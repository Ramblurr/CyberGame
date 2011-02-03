package yao.gamelib;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import com.sun.mail.imap.IMAPSSLStore;

public class FetchDataset {
    private String mUsername;
    private String mPassword;
    private String mServer;
    private int mPort;
    private String mBaseDir;

    private Properties mProperties = new Properties();
    private Session mSession = Session.getDefaultInstance(mProperties, null);
    private IMAPSSLStore mImap = new IMAPSSLStore(mSession, null);
    private Logger mLogger = null;
    private FileHandler mFh;

    /**
     * Class encapsulates connection to the email server and provides methods to download email as a dataset
     * It connects to the server using IMAP.
     * @param username username of email account
     * @param password password of email account
     * @param server IMAP server hostname, e.g., mail.vt.edu
     * @param port server port, e.g., 993
     * @param basedir the absolute path to a base directory to store logs and mail
     */
    public FetchDataset(String username, String password, String server,
            int port, String basedir ) {
        this.mUsername = username;
        this.mPassword = password;
        this.mServer = server;
        this.mPort = port;
        this.mBaseDir = basedir;

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
    private boolean CreateConnection() {
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

    private Folder GetInbox() throws MessagingException {
        Folder inbox = mImap.getFolder("inbox");
        inbox.open(Folder.READ_ONLY);
        return inbox;
    }

    private Store PrepareMaildir() throws NoSuchProviderException {
        mProperties.put("mail.store.maildir.autocreatedir", "true");
        String maildir_url = "maildir:" + mBaseDir + "/" + mUsername +"/Maildir";
        mLogger.log( Level.INFO, "Creating Maildir: "+ maildir_url);
        Store store = mSession.getStore(new URLName(maildir_url));
        return store;
    }

    public boolean DownloadMail() {
        Store store;
        Folder remote_inbox;
        Folder local_inbox;

        mLogger.log( Level.INFO, "Establishing Connection...");
        if( !CreateConnection() ) {
            mLogger.log( Level.SEVERE, "Could not connect. Aborting.");
            return false;
        }
        mLogger.log( Level.INFO, "Connection established.");


        try {
            mLogger.log( Level.INFO, "Preparing local Maildir");
            store = PrepareMaildir();
            mLogger.log( Level.INFO, "Getting remote inbox");
            remote_inbox = GetInbox();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            mLogger.log( Level.SEVERE, "Could not create Maildir.");
            return false;
        } catch ( MessagingException e ) {
            e.printStackTrace();
            mLogger.log( Level.SEVERE, "Could not retrieve remote inbox");
            return false;
        }

        try {
            mLogger.log( Level.INFO, "Getting local INBOX folder");
            local_inbox = store.getFolder("INBOX");
        } catch( MessagingException e ) {
            e.printStackTrace();
            mLogger.log( Level.SEVERE, "Could not retrieve local inbox");
            return false;
        }


        Message messages[];
        try {
            mLogger.log( Level.INFO, "Getting messages");
            messages = remote_inbox.getMessages();
        } catch (MessagingException e) {
            e.printStackTrace();
            mLogger.log( Level.SEVERE, "Could not retrieve remote messages");
            return false;
        }
        try {
            mLogger.log( Level.INFO, "Saving messages locally");
            local_inbox.appendMessages(messages);
        } catch (MessagingException e) {
            e.printStackTrace();
            mLogger.log( Level.SEVERE, "Could not append local messages");
            return false;
        }
        mLogger.log( Level.INFO, "Download operation complete.");
        return true;
    }
}
