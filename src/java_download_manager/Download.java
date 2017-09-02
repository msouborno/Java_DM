/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * nlkhand open the template in the editor.
 */
package java_download_manager;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

/**
 *
 * @author hp
 */
class Download  extends Observable implements Runnable{

    private static final int MAX_BUFFER_SIZE=1024;
    public static final String STATUSES[]={"Downloading","Paused","Complete","Error"};
    public static final  int Downloading=0;
     public static final  int PAUSED=1;
      public static final  int COMPLETE=2;
       public static final  int CANCELLED=3;
        public static final  int ERROR=4;
        private URL url;
        private int size,download,status;
        public Download(URL url)
        {
            this.url=url;
            size=-1;
            download=0;
            status=Downloading;
            download();
        }
        public String getUrl()
        {
            return url.toString();
        }
        public int get_size()
        {        return size;
        }
        public float getProgress()
        {
            return ((float)download/size)*100;
        }
        public int get_status()
        {
            return status;
        }
        public void pause()
        {
            status=PAUSED;
            statusChanged();
            
        }
        public void resume()
        {
            status=Downloading;
            statusChanged();
            download();
        }
        public void cancel()
        {
            status=CANCELLED;
            statusChanged();
        }
        public void error()
        {
            status=ERROR;
            statusChanged();
        }
            
    private void download() {
        Thread thread=new Thread();
        thread.start();
    }
        
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    private String getFileName(URL url)
    {
        String filename=url.getFile();
        return filename.substring(filename.lastIndexOf('/')+1);
        
    }
    public void run()
    {
        RandomAccessFile file=null;
        InputStream stream=null;
        try{
            HttpURLConnection  conection=( HttpURLConnection) url.openConnection();
            conection.setRequestProperty("Range","bytes="+download+" - ");
            conection.connect();
            int contentlength=conection.getContentLength();
            if(size==-1)
            {
                size=contentlength;
                statusChanged();
            }
            file=new RandomAccessFile(getFileName(url),"rw");
            file.seek(download);
            stream=conection.getInputStream();
            while(status==Downloading)
            {
                byte buffer[];
                if(size-download>MAX_BUFFER_SIZE)
                {
                    buffer=new byte[MAX_BUFFER_SIZE];
                }
                else
                    buffer=new byte[size-download];
                int read=stream.read(buffer);
                if(read==-1)
                    break;
                file.write(buffer,0,read);
                download+=read;
                statusChanged();
                if(status==Downloading)
                {
                    status=COMPLETE;
                    statusChanged();
                }
    }
        }
        
        catch(Exception e)
            
        {
            error();
    }
        finally
        {
            if(file!=null)
            {
                try
                {
                    file.close();
                }
                catch(Exception e)
                {
                }
                
            }
                }
            }
    
    private void statusChanged() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    setChanged();
    notifyObservers();
    
    
    }
    
    
        
        
    
    
}
