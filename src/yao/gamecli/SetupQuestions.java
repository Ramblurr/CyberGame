package yao.gamecli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import yao.gamelib.EmailStore;
import yao.gamelib.FetchDataset;
import yao.gamelib.FromSubjectFactory;
import yao.gamelib.FromWhenFactory;
import yao.gamelib.FromWhomFactory;
import yao.gamelib.Question;
import yao.gamelib.QuestionGenerator;
import yao.gamelib.SentSubjectFactory;
import yao.gamelib.SentWhenFactory;
import yao.gamelib.SentWhomFactory;
import yao.gameweb.util.Database;

public class SetupQuestions {

    static String username;
    static String basepath;
    static String[] folders;

    /**
     * @param args
     */
    public static void main(String[] args) {
        String arg;
        int i = 0;
        boolean verbose = false, fetch_only = false, generate_only = false;
        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];
            if (arg.equals("-verbose")) {
                verbose = true;
            } else if (arg.equals("-fetch")) {
                fetch_only = true;
            } else if (arg.equals("-generate")) {
                generate_only = true;
            } else {
                printUsage();
                System.exit(1);
            }
        }

        if (generate_only && fetch_only) {
            System.err.println("Error: -fetch and -generate are mutually exclusive options.");
            System.exit(1);
        }
        if (fetch_only) {
            fetch(verbose);
            System.exit(0);
        }
        if (generate_only) {
            generate(verbose);
            System.exit(0);
        }
        fetch(verbose);
        generate(verbose, username, basepath, folders);
        

    }

    public static void generate(boolean verbose) {
        username = readString("Username: ").trim();
        String curr_dir = System.getProperty("user.dir");
        basepath = readString("Dataset path [" + curr_dir + "]: ").trim();
        if (basepath.length() == 0) {
            basepath = curr_dir;
        }
        System.out.println("Which folders should be used? The default is inbox and sent mail.");
        String folders_raw = readString("Folders (comma separated) [inbox, sent]: ").trim();
        folders = null;
        if (folders_raw.length() != 0) {
            if (verbose)
                System.out.print("Using folders: ");
            folders = folders_raw.split(",");
            for (int j = 0; j < folders.length; j++) {
                folders[j] = folders[j].trim();
                if (verbose)
                    System.out.print("'" + folders[j] + "'  ");
            }
            if (verbose)
                System.out.println("");
        } else {
            if (verbose)
                System.out.println("Using folders: 'inbox' 'sent'");
            folders = new String[2];
            folders[0] = "inbox";
            folders[1] = "sent";
        }
        generate(verbose, username, basepath, folders);
    }

    public static void generate(boolean verbose, String user, String base, String[] f) {
        EmailStore store = null;
        String[] folders_normalized = new String[f.length]; //  
        try {
            store = new EmailStore(user, base, f);
        } catch (Exception e) {
            System.err.println("Could not open local dataset. Is the basepath, user, and folder list correct?");
            System.exit(1);
        }
        
        System.out.println("To generate questions I need to know which folders contains ");
        System.out.println("mail you *received* and mail you *sent*.");
        String inbox = "inbox", sent = "sent";
        for (int i = 0; i < f.length; i++) {
            boolean received = readBool("Does '" + f[i] + "' contain mail you recevied [y/n]? ");
            if (received)
                inbox = f[i];
            else
                sent = f[i];
        }

        int num = readInt("Number of questions to generate: ");
        boolean cont = false;
        Question[] questions = null;
        do {
            QuestionGenerator gen = new QuestionGenerator();
            gen.registerType(Question.Type.FromWhen, new FromWhenFactory(store, sent, inbox));
            gen.registerType(Question.Type.FromWhom, new FromWhomFactory(store, sent, inbox));
            gen.registerType(Question.Type.FromSubject, new FromSubjectFactory(store, sent, inbox));
            gen.registerType(Question.Type.SentSubject, new SentSubjectFactory(store, sent, inbox));
            gen.registerType(Question.Type.SentWhen, new SentWhenFactory(store, sent, inbox));
            gen.registerType(Question.Type.SentWhom, new SentWhomFactory(store, sent, inbox));
    
            questions = gen.createQuestionsEven(num);
            System.out.println("made " + questions.length + " questions");
            for (Question q : questions) {
                if( q == null ) {
                    System.out.println("WTF NULL!");
                    continue;
                }
                System.out.println(q.getType());
                System.out.println("\tQ:" + q.getQuestion());
                System.out.println("\tA(real):" + q.getAnswer());
                for (String fake : q.getFakeAnswers()) {
                    System.out.println("\tA:" + fake);
                }
            }
            cont = readBool("Re-generate  [y/n]? ");
        } while (cont);

        Database db = Database.getInstance();
        for (Question q : questions) {
            int question_id = db.insertQuestion(q);
        }
    }
    public static void fetch(boolean verbose) {
        // first read user credentials
        System.out.println("Email Credentials");
        username = readString("Username: ").trim();
        System.out.println("Enter your email address if it is different than your username, otherwise hit enter.");
        String email_str = readString("Email: ").trim();
        String password = readString("Account Password: ");

        if (email_str.length() == 0)
            email_str = username;

        InternetAddress email = null;
        try {
            email = new InternetAddress(email_str);
        } catch (AddressException e) {
            System.err.println("Parsing your email address failed: " + e.getMessage());
            System.exit(1);
        }

        String server = "";
        int port = 143;

        int at_sign_loc = email_str.lastIndexOf('@');
        String host = email_str.substring(at_sign_loc + 1, email_str.length());

        if (host.equals("gmail.com")) {
            if (verbose)
                System.out.println("Gmail address detected.");
            server = "imap.gmail.com";
            port = 993;
        } else {
            server = readString("Server: ").trim();
            port = readInt("Port: ");
        }

        String curr_dir = System.getProperty("user.dir");
        basepath = readString("Path to save dataset [" + curr_dir + "]: ").trim();
        if (basepath.length() == 0) {
            basepath = curr_dir;
        }
        if (verbose)
            System.out.println("Saving data to " + basepath);
        System.out.println("Which folders do you want to retrieve? The default is inbox and sent mail.");
        String folders_raw = readString("Folders to retrieve (comma separated) [inbox, sent]: ").trim();
        folders = null;
        if (folders_raw.length() != 0) {
            if (verbose)
                System.out.print("Using folders: ");
            folders = folders_raw.split(",");
            for (int j = 0; j < folders.length; j++) {
                folders[j] = folders[j].trim();
                if (verbose)
                    System.out.print("'" + folders[j] + "'  ");
            }
            if (verbose)
                System.out.println("");
        } else {
            if (verbose)
                System.out.println("Using folders: 'inbox' 'sent'");
        }
        System.out.println("Starting fetch proccess. Logging status to file: " + basepath + "/" + username + ".log");
        FetchDataset fetcher = new FetchDataset(username, password, server, port, basepath, folders);
        fetcher.DownloadMail();
        System.out.println("Fetching Complete. Check status log: " + basepath + "/" + username + ".log");
    }

    public static void printUsage() {
        System.err.println("usage: SetupQuestions [-verbose] [-fetch]");
        System.err.println("This interactive utility fetches an email dataset and");
        System.err.println("then generates questions from the dataset");
        System.err.println("Options:");
        System.err.println("\t-verbose  \tinclude extra debug statements");
        System.err.println("\t-fetch    \tonly fetch the dataset, do not generate questions");
        System.err.println("\t-generate \tuse an existing dataset to generate questions");
    }

    public static String readString(String prompt) {
        System.out.print(prompt);
        String input = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            input = br.readLine();
        } catch (IOException ioe) {
            System.err.println("IO error trying to read input.");
            System.exit(1);
        }
        return input;
    }

    public static int readInt(String prompt) {
        System.out.print(prompt);
        int value = -1;
        String input = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            input = br.readLine();
        } catch (IOException ioe) {
            System.err.println("IO error trying to read input.");
            System.exit(1);
        }
        try {
            value = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse integer from user input.");
            System.exit(1);
        }
        return value;
    }

    public static boolean readBool(String prompt) {
        System.out.print(prompt);
        String input = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                input = br.readLine().trim();
            } catch (IOException ioe) {
                System.err.println("IO error trying to read input.");
                System.exit(1);
            }
            if (input.length() == 0)
                System.out.println("Input must be 'yes' or 'no'");
            else {
                input = input.toLowerCase();
                char c = input.charAt(0);
                if (c == 'y')
                    return true;
                else if (c == 'n')
                    return false;
            }
        }
    }
}
