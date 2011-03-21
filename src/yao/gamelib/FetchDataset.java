package yao.gamelib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import com.sun.mail.imap.IMAPSSLStore;

public class FetchDataset {
    private final String mUsername;
    private final String mPassword;
    private final String mServer;
    private final int mPort;
    private final String mBaseDir;
    private final String[] mFolders;
    private final int mMsgMax;

    private final Properties mProperties = new Properties();
    private final Session mSession = Session.getDefaultInstance(mProperties, null);
    private final IMAPSSLStore mImap = new IMAPSSLStore(mSession, null);
    private Logger mLogger = null;
    private FileHandler mFh;

    /**
     * Class encapsulates connection to the email server and provides methods to download email as a dataset It connects to the server using IMAP.
     *
     * @param username
     *            username of email account
     * @param password
     *            password of email account
     * @param server
     *            IMAP server hostname, e.g., mail.vt.edu
     * @param port
     *            server port, e.g., 993
     * @param basedir
     *            the absolute path to a base directory to store logs and mail
     * @param folders
     *            an array of foldernames to download, if null will default to inbox and sent folder
     * @param maximum
     *            the maximum number of messages to download from each folder
     */
    public FetchDataset(String username, String password, String server,
 int port, String basedir, String[] folders, int maximum) {
        this.mUsername = username;
        this.mPassword = password;
        this.mServer = server;
        this.mPort = port;
        this.mBaseDir = basedir;
        this.mFolders = folders;
        this.mMsgMax = maximum;

        mLogger = Logger.getLogger(mUsername );
        try {
            mFh = new FileHandler(mBaseDir + "/" + mUsername + ".log", true);
            mLogger.addHandler(mFh);
            mLogger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            mFh.setFormatter(formatter);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date curDate = new Date(System.currentTimeMillis());
        mLogger.log(Level.INFO, curDate.toString());
    }

    /**
     * Attempts to make connection to the server
     * @return true on successful connection, false otherwise
     */
    public boolean CreateConnection() {
        try {
            mImap.connect(mServer, mPort, mUsername, mPassword);
        } catch( AuthenticationFailedException e ) {
            e.printStackTrace();
            mLogger.log( Level.SEVERE, "Authentication Failed");
            return false;
        } catch (MessagingException e) {
            e.printStackTrace();
            mLogger.log( Level.SEVERE, "Something failed during connection");
            return false;
        } catch ( IllegalStateException e ) {
            e.printStackTrace();
            mLogger.log( Level.SEVERE, "IllegalStateException during connection");
            return false;
        }
        return true;
    }

    private Folder GetFolder(String name) throws MessagingException {
        Folder folder = mImap.getFolder(name);
        folder.open(Folder.READ_ONLY);
        return folder;
    }

    private Folder GetInbox() throws MessagingException {
        return GetFolder("inbox");
    }

    public Folder GetSentFolder() throws MessagingException {
        Folder DF;
        Folder sent = null;
        Folder[] list;
        mLogger.log( Level.INFO, "Searching for sent folder");
        DF = mImap.getDefaultFolder();
        list = DF.list();

        list = DF.list("*sent*");
        if( list.length == 0 ) {
            list = DF.list("*Sent*");
        }
        if( list.length == 0 ) {
            mLogger.log( Level.WARNING, "Could not find a sent mail folder.");
            return null;
        }
        sent = list[0];
        mLogger.log( Level.INFO, "Found sent folder: '"+sent.getName()+"'");
        sent.open(Folder.READ_ONLY);
        return sent;
    }

    private Store PrepareMaildir() throws NoSuchProviderException {
        mProperties.put("mail.store.maildir.autocreatedir", "true");
        String maildir_url = FetchDataset.MailDirDir(mBaseDir, mUsername);
        mLogger.log( Level.INFO, "Creating Maildir: "+ maildir_url);
        Store store = mSession.getStore(new URLName(maildir_url));
        return store;
    }

    public static String MailDirDir(String baseurl, String username) {
        return "maildir:" + baseurl + "/" + username +"/Maildir";
    }

    public boolean DownloadMail() {
        Store store;
        Folder[] remote_folders = null, local_folders = null;
        if( mFolders != null ) {
            remote_folders = new Folder[mFolders.length];
            local_folders = new Folder[mFolders.length];
        }
        Folder remote_inbox = null, local_inbox = null;
        Folder remote_sent = null, local_sent = null;

        mLogger.log(Level.FINE, "Establishing Connection...");
        if( !CreateConnection() ) {
            mLogger.log( Level.SEVERE, "Could not connect. Aborting.");
            return false;
        }
        mLogger.log( Level.INFO, "Connection established.");


        try {
            mLogger.log(Level.FINE, "Preparing local Maildir");
            store = PrepareMaildir();
            mLogger.log( Level.INFO, "Getting remote folders");
            if (mFolders != null) {
                for (int i = 0; i < mFolders.length; ++i) {
                    mLogger.log(Level.FINE, "\tscreating remote folder for" + mFolders[i]);
                    remote_folders[i] = GetFolder(mFolders[i]);
                }
            } else {
                remote_inbox = GetInbox();
                remote_sent = GetSentFolder();
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            mLogger.log( Level.SEVERE, "Could not create Maildir.");
            return false;
        } catch ( MessagingException e ) {
            e.printStackTrace();
            mLogger.log( Level.SEVERE, "Could not retrieve remote folders");
            return false;
        }

        try {
            mLogger.log( Level.INFO, "Getting local folders");
            if (mFolders != null) {
                for (int i = 0; i < mFolders.length; ++i) {
                    mLogger.log(Level.FINE, "\tscreating local folder for" + mFolders[i]);
                    local_folders[i] = store.getFolder(mFolders[i]);
                }
            } else {
                local_inbox = store.getFolder("INBOX");
                local_sent = store.getFolder("SENT");
            }
        } catch( MessagingException e ) {
            e.printStackTrace();
            mLogger.log( Level.SEVERE, "Could not retrieve local folders");
            return false;
        }


        Message[][] messages = null;
        if (mFolders != null) {
            messages = new Message[mFolders.length][];
        }
        Message inbox_messages[] = null, sent_messages[] = null;
        try {
            mLogger.log( Level.INFO, "Getting messages");
            if (mFolders != null) {
                for (int i = 0; i < mFolders.length; ++i) {
                    mLogger.log(Level.FINE, "\tsdownloading messages for " + mFolders[i]);
                    messages[i] = remote_folders[i].getMessages();
                }
            } else {
                inbox_messages = remote_inbox.getMessages();
                sent_messages = remote_sent.getMessages();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            mLogger.log( Level.SEVERE, "Could not retrieve remote messages");
            return false;
        }
        //        mLogger.log(Level.FINE, "Cleaning message list.");
        //        inbox_messages = CleanMessages(inbox_messages);

        try {
            mLogger.log( Level.INFO, "Saving messages locally");
            if (mFolders != null) {
                for (int i = 0; i < mFolders.length; ++i) {
                    mLogger.log(Level.FINE, "\tsaving messages for " + mFolders[i]);
                    local_folders[i].appendMessages(TrimMessages(messages[i]));
                }
            } else {
                local_inbox.appendMessages(TrimMessages(inbox_messages));
                local_sent.appendMessages(TrimMessages(sent_messages));
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            mLogger.log( Level.SEVERE, "Could not append local messages");
            return false;
        }
        mLogger.log( Level.INFO, "Download operation complete.");
        return true;
    }

    private Message[] TrimMessages(Message[] msgs) {
//        System.out.println("Trim: Before " + msgs.length);
        if (mMsgMax <= 0 || msgs.length <= mMsgMax) {
//            System.out.println("Trim: no need to trim, aborting");
            return msgs;
        }

        Message[] smaller = new Message[mMsgMax];
        // We want to traverse backwards, because newer messages are at the end
        for (int i = msgs.length - 1, j = 0; j < smaller.length && i > 0; --i, ++j) {
            smaller[j] = msgs[i];
        }
//        System.out.println("Trim: after " + smaller.length);
        return smaller;
    }

    private Message[] CleanMessages(Message[] msgs) {
        ArrayList<Message> cleaned = new ArrayList<Message>();
        for( Message msg : msgs ) {
            try {
                Flags flags =  msg.getFlags();
                if( flags.contains(Flags.Flag.SEEN) ) {

                    cleaned.add(msg);
                }
            } catch (MessagingException e){
                e.printStackTrace();
                mLogger.log( Level.WARNING, "Flag retrieval failed");
            }
        }
        Message[] cleaned_msgs = new Message[cleaned.size()];
        return cleaned.toArray(cleaned_msgs);
    }

    public void ListFolders() {
        Folder DF;
        Folder[] list;
        mLogger.log( Level.INFO, "Listing Folders");
        try {
            DF = mImap.getDefaultFolder();
        } catch (MessagingException e) {
            e.printStackTrace();
            return;
        }

        try {
            list = DF.list();
        } catch (MessagingException e) {
            e.printStackTrace();
            return;
        }
        for(Folder f :list ) {
            mLogger.log( Level.INFO, f.getName() );
        }
    }
}
