package yao.gamelib;

import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;

/**
 * Encapsulates the email store on disk. Can be used to retrieve messages for processing.
 * @author Casey
 *
 */
public class EmailStore {
    String mUsername;
    String mBaseDir;
    String mMailDirLoc;
    Store mStore;
    
    private class FolderInfo {
        public FolderInfo(Folder f) throws MessagingException {
            folder = f;
            seenMarks = new BitSet(f.getMessageCount());
            
            reset();
        }
        public void reset() {
            lastSeen = -1;
            seenMarks.clear();
        }
        public Folder folder;
        public int lastSeen;
        public BitSet seenMarks;
    }
    
    static final Map<String, FolderInfo> mFolderMap = new HashMap<String, FolderInfo>();
      
    /**
     * Loads the EmailStore for the specified user at the given base directory.
     * @param username
     * @param basedir
     * @throws MessagingException 
     */
    public EmailStore(String username, String basedir) throws Exception {
        mUsername = username;
        mBaseDir = basedir;
        mMailDirLoc = FetchDataset.MailDirDir(basedir, username);
        
        Session session = Session.getInstance(new Properties());
        mStore = session.getStore(new URLName(mMailDirLoc));
        mStore.connect(); //useless with Maildir but included here for consistency
        
        //open the inbox
        openFolder("inbox");
        //open the sent folder
        openFolder( "sent" );
    }
    
    /**
     * Returns a message that has not yet been seen by the client of this object. 
     * The state lasts online the lifetime of the object.
     * The folder used is inbox.
     * @return an unseen message from the inbox, could be null
     */
    public Message getNewMessageInbox() {
        return getNewMessage("inbox");
    }
    
    /**
     * Returns a message that has not yet been seen by the client of this object. 
     * The state lasts online the lifetime of the object.
     * The folder used is inbox.
     * @return an unseen message from the inbox, could be null
     */
    public Message getNewMessageSent() {
        return getNewMessage("sent");
    }

    /**
     * Returns a message that has not yet been seen by the client of this object. 
     * The state lasts online the lifetime of the object.
     * @param folder the folder to retrieve a message from.
     * @return an unseen message, could be null
     */
    public Message getNewMessage(String folder) {
        if(!mFolderMap.containsKey(folder)){
            return null;
        }
        FolderInfo info = mFolderMap.get(folder);
        int inboxnum = 0;
        try {
            inboxnum = info.folder.getMessageCount();
        } catch (MessagingException e1) {
            e1.printStackTrace();
            assert false; //TODO change this assert probably
        }
        int l = info.seenMarks.length();
        for( int i = info.seenMarks.size(); i > 0; i--) {
            if( info.seenMarks.get(i) )
                continue;
            info.lastSeen = i;
        }
        if( info.lastSeen > inboxnum ) {
            return null;
        }
        try {
            Message msg = info.folder.getMessage(info.lastSeen);
            info.seenMarks.set(info.lastSeen, true);
            return msg;
        } catch (MessagingException e) {
            e.printStackTrace();
            assert false; //TODO change this assert probably
        }
        return null;
    }
    
    public Message[] getMessageInRange( String folder, Date beginDate,
            Date endDate ) {
        if ( !mFolderMap.containsKey( folder ) ) {
            return null;
        }
        FolderInfo info = mFolderMap.get( folder );
        SearchTerm afterTerm = new SentDateTerm( ComparisonTerm.GE, beginDate );
        SearchTerm beforeTerm = new SentDateTerm( ComparisonTerm.LE, endDate );
        SearchTerm andTerm = new AndTerm( afterTerm, beforeTerm );
        try {
            return info.folder.search( andTerm );
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void resetInbox() {
        resetFolder("inbox");
    }
    /** 
     * Resets the internal state
     * @param folder
     */
    public void resetFolder(String folder) {
        if( mFolderMap.containsKey(folder)){
            FolderInfo info = mFolderMap.get(folder);
            info.reset();
        }
    }
    public int getMessageCountInbox() {
        return getMessageCount("inbox");
    }
    public int getMessageCount(String folder) {
        if( mFolderMap.containsKey(folder)){
            FolderInfo info = mFolderMap.get(folder);
            try {
                return info.folder.getMessageCount();
            } catch (MessagingException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }
    
    public String getUsername() {
        return mUsername;
    }
    
    /**
     * Opens the folder for reading with this class. By default the inbox is opened on class instantiation.
     * @param folder_name
     * @throws MessagingException
     */
    public void openFolder(String folder_name) throws MessagingException {
        Folder folder = mStore.getFolder(folder_name);
        folder.open(Folder.READ_ONLY);
        FolderInfo info = new FolderInfo(folder);
        mFolderMap.put(folder_name, info);
    }

}
