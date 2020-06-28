# File-Sync-J

English | [中文](https://github.com/codflow/file-sync-j/file-sync-j-jui/blob/master/README_zh.MD)

File-Sync-J is a file-level synchronization program in Java language.As a intermediary,It sync file in different mode between two filesystem endpoints (One as target while another as source).It support traditional file transfer protocols(SFTP,FTP,SMB,etc) and modern file service(OSS).The author's goal is build a sync program supporting most popular file transfer protocols based on a high adaptable sync framework.


## Getting Started

Start the program in java environment.

```
    java -jar file-sync-j-jui-0.0.1-alpha.jar
```

### Add Endpoint

  ![step1](https://raw.githubusercontent.com/codflow/file-sync-j/master/file-sync-j-jui/doc/images/main_ui_0.png)

    1.Click "Add Endpoint" button at main ui.
    
    2.Select endpoint type (local and sftp are supported.more types are coming soon).

    3.Click "Test" to test endpoint. There would be a green circle next to type

  ![step4](https://raw.githubusercontent.com/codflow/file-sync-j/file-sync-j-jui/master/doc/images/endpoint_sftp_0.png)
  
    4.Click "Add".

### Sync Endpoint

    
  ![step1](https://raw.githubusercontent.com/codflow/file-sync-j/file-sync-j-jui/master/doc/images/main_ui_1.png)

    1.Choose a Source Endpoint and a Destination Endpoint(They can not be the same one).

    2.Click Sync.

## Mode

    There are two modes supported now:
    
    SYNC: keep destination endpoint as same as source enpoint

    INC: fetch new file from source to destination but not delete files which removed