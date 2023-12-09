Uno Game Project - Milestone #4
[Version 1.40]
December 8th 2023

-------------------------------Overview--------------------------------------
This milestone marks the fourth phase of our Uno card game project. Our goal is to integrate uno flip into out already existing GUI-based, 
playable version of Uno that allows players to engage through a graphical display using mouse input. We are including AI player capability 
which allows for a select number of AI players based on user's choice. We have now included a mechanism where players can save their progress 
in the game and load it when they choose to do so with java serialization and deserialization. The functionality to redo moves, undo moves and restart the game 
has been integrated as well.

-------------------------------Deliverables-----------------------------------
1. Game Features
View drawn cards.
Place cards using official notation.
Draw one card.
Execute actions associated with special cards.
Observe the resultant state of cards in GUI format.
use added feature of flip cards.
Undo functionality.
Redo functionality.
Replay capability.
Save and load functionality.
2. tests
3. Documentation
Documentation on how the interfaces and interactions between loosely coupled components.
Instructions that include details on how to extend or modify functionalities without disrupting existing modules.

------------------------------Documentation----------------------------------
UML modeling of the problem domain.
Class diagrams with complete variable and method signatures.
Sequence diagrams for important scenarios.
relevant operations for the Model part of the MVC.
Documentation on how the interfaces and interactions between loosely coupled components.
Instructions that include details on how to extend or modify functionalities without disrupting existing modules.

-------------------------------Known Issues----------------------------------

Possible incomplete implementation, but remaining tasks will be documented.

----------------------------------Authors------------------------------------

Jonas Hallgrimsson 101223596
Jawad Mohammed 101233031
Quentin Weir 101234808
Omar Hamzat 101244220


-----------------------Changes Since Previous Deliverable--------------------
Undo functionality.
Redo functionality.
Replay capability.
Save and load functionality.

-----------------------How new functionalities were implemented--------------------
Undo functionality: we implemented an undo button in the GUI (undoButton),
allowing players to revert the game state to the previous turn. The undo() method in the
UnoGUI class manages this functionality, utilizing the SaveLoad class to store and retrieve multiple game
states.

Redo functionality: this was implemented to act just like the previous functionality of being able to play a card.
once the undo button is pressed then the player can redo their move.

Save and load functionaity: Using serialization of the game's current state,
enabling players to save and load their progress. The serialized data includes crucial information such as
player details, deck states, AI player configurations, flipped and current decks, top card, player counts,
and game-specific flags


