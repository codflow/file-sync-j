package ink.codflow.sync.core.adapter;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.NameScope;
import org.apache.commons.vfs2.util.FileObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.task.SyncFileWorkerHandler;

public class Vfs2VfsObjectManipulationAdapter implements ObjectManipulationAdapter<FileObject, FileObject> {
	
	private static final Logger log = LoggerFactory.getLogger(Vfs2VfsObjectManipulationAdapter.class);
	public static final AdapterTypeEnum TYPE_TAG_0 = AdapterTypeEnum.VFS2VFS;

	
	@Override
	public void copy(FileObject src, FileObject dest) throws FileException {

		FileType srcType;
		FileType destType;
		log.trace("File:{} synced",src.getName().getBaseName());
		try {
			srcType = src.getType();
			destType = dest.getType();
		} catch (FileSystemException e) {
			throw new FileException();
		}
		if (FileType.FILE.equals(srcType)) {

			if (FileType.FILE.equals(destType)||FileType.IMAGINARY.equals(destType)) {
				copyFileToFile(src, dest);

			} else if (FileType.FOLDER.equals(destType)) {
				copyFileToDir(src, dest);
			}
		} else if (FileType.FOLDER.equals(srcType) && (FileType.FOLDER.equals(destType) || FileType.IMAGINARY.equals(destType))) {
			copyDirToDir(src, dest);
		} else {
			throw new FileException();

		}
	}

	public void copyFileToFile(FileObject srcFile, FileObject destFile) throws FileException {

		try {
			FileObjectUtils.writeContent(srcFile, destFile);
		} catch (IOException e) {
			throw new FileException(e);
		}

	}

	public void copyFileToDir(FileObject srcFile, FileObject destDir) throws FileException {
		String fileName = srcFile.getName().getBaseName();
		FileObject destFile0;
		try {
			destFile0 = destDir.resolveFile(fileName);
			FileObjectUtils.writeContent(srcFile, destFile0);

		} catch (IOException e) {
			throw new FileException();
		}

	}

	public void copyDirToDir(FileObject srcDir, FileObject destDir) throws FileException {

		AllFileSelector selector = new AllFileSelector();

		try {

			if (!FileObjectUtils.exists(srcDir)) {
				throw new FileSystemException("vfs.provider/copy-missing-file.error", srcDir);
			}

			// Locate the files to copy across
			final ArrayList<FileObject> files = new ArrayList<>();
			srcDir.findFiles(selector, false, files);

			// Copy everything across
			for (final FileObject srcFile : files) {
				// Determine the destination file
				final String relPath = srcDir.getName().getRelativeName(srcFile.getName());
				final FileObject destFile0 = destDir.resolveFile(relPath, NameScope.DESCENDENT_OR_SELF);

				// Clean up the destination file, if necessary
				if (FileObjectUtils.exists(destFile0) && destFile0.getType() != srcFile.getType()) {
					// The destination file exists, and is not of the same type,
					// so delete it
					// TODO - add a pluggable policy for deleting and overwriting existing files
					destFile0.deleteAll();
				}

				// Copy across
				try {
					if (srcFile.getType().hasContent()) {
						FileObjectUtils.writeContent(srcFile, destFile0);
						long modTime = srcFile.getContent().getLastModifiedTime();
						destFile0.getContent().setLastModifiedTime(modTime);
					} else if (srcFile.getType().hasChildren()) {
						destFile0.createFolder();
						// change dir ts ?
						// long modTime = srcFile.getContent().getLastModifiedTime();
						// destFile.getContent().setLastModifiedTime(modTime);
					}
				} catch (final IOException e) {

					throw new FileSystemException("vfs.provider/copy-file.error", e, srcFile, destFile0);
				}
			}

		} catch (Exception e) {
			throw new FileException();
		}
	}

	public boolean checkDiff(FileObject fileObject0, FileObject fileObject1) throws FileException {

		try {
			long obj0ModTime = fileObject0.getContent().getLastModifiedTime();
			long obj1ModTime = fileObject1.getContent().getLastModifiedTime();
			int compareInt = Long.compare(obj0ModTime, obj1ModTime);
			if (compareInt > 0) {
				return true;
			}
			return false;
		} catch (FileSystemException e) {
			throw new FileException();
		}
	}

	@Override
	public Class<FileObject> getSrcClassType() {
		return FileObject.class;
	}

	@Override
	public Class<FileObject> getDestClassType() {
		return FileObject.class;
	}



}
