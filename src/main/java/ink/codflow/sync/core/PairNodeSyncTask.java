package ink.codflow.sync.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PairNodeSyncTask implements RunnableFuture<PairSyncResult> {

    

    PairNodeSyncTask(ObjectPairNode node) {
    	
    }

    @Override
    public boolean cancel(boolean arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public PairSyncResult get() throws InterruptedException, ExecutionException {
        // TODO Auto-generated method stub
        
        BlockingQueue<String>ddBlockingQueue;
         return null;
    }

    @Override
    public PairSyncResult get(long arg0, TimeUnit arg1) throws InterruptedException, ExecutionException, TimeoutException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isCancelled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isDone() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void run() {


        // TODO Auto-generated method stub

    }
}