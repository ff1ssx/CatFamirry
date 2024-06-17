## Project Description
This game involves managing a virtual environment where players interact with various elements such as customers, items, and waste. The goal is to manage the shop efficiently by attending to customers, cleaning up waste, and ensuring customer satisfaction, which in turn affects your reputation and money.

## Partner Responsibilities
- **Harry Lee**:
    - Implemented the core gameplay mechanics, including customer movement and interaction logic.
    - Integrated the search and sorting features for items and customers.
    - Developed the waste management and spawning system.

- **Sherry Sun**:
    - Designed the shop interface and implemented item purchasing functionality.
    - Managed the different game states and the corresponding screens.
    - Handled the graphical rendering including loading and displaying images for various game elements.

## How to Play
1. **Starting the Game**: Launch the game by running the `Driver` class.
2. **Navigating the Menu**: Use the menu buttons to start the game, view instructions, or learn about the game.
3. **Managing Customers**:
    - Customers will enter the shop and head to the cashier to pay.
    - By default, customers have a 20% chance to interact with an item/cat when they are adjacent to it.
    - Additionally, you can click on a customer when they are in adjacent tiles to an item/cat to activate an interaction if done in time.
    - The amount of money you get from each interaction corresponds to the price of the item (more expensive items generate more money).
    - When customers' satisfaction (marked by the number above them) goes to 0, they leave the cafe.
4. **Cleaning Waste**:
    - Waste will spawn periodically in the shop.
    - Click on the waste to remove it and increase your reputation.
5. **Using the Shop**:
    - Click on the shop button to open the shop interface.
    - Purchase items/cats and place them in the shop to enhance customer experience.

## Hints on How to Play
- Focus on keeping the shop clean to maintain a high reputation.
- Strategically place items/cats to ensure customers interact with them, increasing their satisfaction and your earnings.
- Monitor your money and reputation closely to make informed purchasing decisions.

## Functionalities Missing from Original Plan
- Advanced customer behaviors, such as different preferences for items.
- Detailed employee management system with varied roles and actions.

## Additional Functionalities Added
- Implemented a system to limit the number of customers and waste on the screen.
- Added visual indicators for customer interactions and payments (icons show for a bit beside their satisfaction points).
- Enhanced sorting and search functionalities for the shop interface.

## Known Bugs / Errors
- Customers may overlap when heading to the cashier. This is due to the limitations of a 2D tile-based game. Although customers appear to walk through each other, they are supposed to be avoiding collisions in a natural manner.
- Items can be placed in undesirable places (like in front of the entrance, on the cashier, etc.).
- Waste may spawn in the tiles where items are placed.

## Important Info
- Ensure all image files (e.g., `waste.png`, `Table.png`, etc.) are located in the same directory as the game files.
- The game is best played on a screen resolution of 800x600 to simulate the minimalistic experience.
- Use the provided font file `Neucha-Regular.ttf` for consistent text rendering.

Enjoy managing your virtual shop and keep those customers happy!
