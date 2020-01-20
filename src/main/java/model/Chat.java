package model;

import org.jspace.RemoteSpace;

import java.util.ArrayList;
import java.util.List;

public class Chat implements Runnable {

    private RemoteSpace remoteSpace;
    private List<Object[]> allChats;
    private List<String> newChat = new ArrayList<>();
    private int indexInAllChats;

    Chat(RemoteSpace remoteSpace) {
        this.remoteSpace = remoteSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                allChats = remoteSpace.queryAll(Templates.chat());
                if (allChats.size() > indexInAllChats) {
                    List<Object[]> temp = getNewestChatsFromAllChats(indexInAllChats, allChats.size());
                    newChat.addAll(objectToString(temp));
                    indexInAllChats = allChats.size();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private List<String> objectToString(List<Object[]> objectList) {
        List<String> temp = new ArrayList<>();
        for (Object[] chatEvent : objectList) {
            temp.add(chatEvent[1] + " " + chatEvent[2]);
        }
        return temp;
    }

    private List<Object[]> getNewestChatsFromAllChats(int startIndex, int endIndex) {
        ArrayList<Object[]> temp = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            temp.add(allChats.get(i));
        }
        return temp;
    }

    List<String> getNewChat() {
        return newChat;
    }




}
