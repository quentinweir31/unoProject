import java.io.*;
import java.util.List;

public class SaveLoad implements Serializable {
    private List<Player> players;
    private Player currentPlayer;
    private List<Card> deck;
    private List<AIPlayer> aiPlayers;
    private List<Card> flippedDeck;
    private List<Card> currentDeck;
    private Card topCard;
    private AIPlayer aiPlayer;
    private int playerCount = 0;

    private int aiPlayerCount = 0;
    private volatile Boolean numPlayersSelected = false;
    private int currentPlayerIndex = 0;

    public SaveLoad(String filename){

    }
    public SaveLoad(String filename,List<Player> players, Player currentPlayer, List<Card> deck, List<AIPlayer> aiPlayers, List<Card> flippedDeck, List<Card> currentDeck, Card topCard, AIPlayer aiPlayer, int playerCount, int aiPlayerCount, Boolean numPlayersSelected, int currentPlayerIndex ) {
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.deck = deck;
        this.aiPlayers = aiPlayers;
        this.flippedDeck = flippedDeck;
        this.currentDeck = currentDeck;
        this.topCard = topCard;
        this.aiPlayer = aiPlayer;
        this.playerCount = playerCount;
        this.aiPlayerCount = aiPlayerCount;
        this.numPlayersSelected = numPlayersSelected;
        this.currentPlayerIndex = currentPlayerIndex;


    }

    public void save(String filename){


        try (FileOutputStream fileOut = new FileOutputStream(filename + ".txt");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(this);

            System.out.printf("Serialized data is saved in " + filename + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String filename){

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename+".txt"))) {
            while (true) {
                try {
                    Object obj = ois.readObject();

                    if (obj instanceof SaveLoad) {

                        this.players = ((SaveLoad) obj).getPlayers();
                        this.currentPlayer = ((SaveLoad) obj).getCurrentPlayer();
                        this.deck = ((SaveLoad) obj).getDeck();
                        this.aiPlayers = ((SaveLoad) obj).getAiPlayers();
                        this.flippedDeck = ((SaveLoad) obj).getFlippedDeck();
                        this.currentDeck = ((SaveLoad) obj).getCurrentDeck();
                        this.topCard = ((SaveLoad) obj).getTopCard();
                        this.aiPlayer = ((SaveLoad) obj).getAiPlayer();
                        this.playerCount = ((SaveLoad) obj).getPlayerCount();
                        this.aiPlayerCount = ((SaveLoad) obj).getAiPlayerCount();
                        this.numPlayersSelected = ((SaveLoad) obj).getNumPlayersSelected();
                        this.currentPlayerIndex = ((SaveLoad) obj).getCurrentPlayerIndex();

                    }

                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.printf("Serialized data retrieved from " + filename + ".txt");
    }

    private String getfileName(){
        return "SampleGame";
    }


    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public List<AIPlayer> getAiPlayers() {
        return aiPlayers;
    }

    public List<Card> getFlippedDeck() {
        return flippedDeck;
    }

    public List<Card> getCurrentDeck() {
        return currentDeck;
    }

    public Card getTopCard() {
        return topCard;
    }

    public AIPlayer getAiPlayer() {
        return aiPlayer;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getAiPlayerCount() {
        return aiPlayerCount;
    }

    public Boolean getNumPlayersSelected() {
        return numPlayersSelected;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }


}
