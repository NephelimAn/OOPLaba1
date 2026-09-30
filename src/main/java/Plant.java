import java.util.List;

public class Plant extends Agent {

    private static final int INITIAL_ENERGY = 20;
    private static final int ENERGY_PER_TURN = 4;
    private static final int REPRODUCTION_THRESHOLD = 35;

    public Plant(int x, int y) {
        super(x, y, INITIAL_ENERGY);
    }

    @Override
    public void act(Environment environment) {

        // Растение получает энергию от "солнца"
        energy += ENERGY_PER_TURN;

        // Размножение при достаточном количестве энергии
        if (energy >= REPRODUCTION_THRESHOLD) {

            List<int[]> emptyCells =
                    environment.getEmptyNeighbors(x, y, 1);

            if (!emptyCells.isEmpty()) {

                int[] position = emptyCells.get(
                        Environment.RANDOM.nextInt(emptyCells.size())
                );

                environment.addAgent(
                        new Plant(position[0], position[1])
                );

                energy -= INITIAL_ENERGY;
            }
        }
    }

    @Override
    public char getSymbol() {
        return 'Р';
    }
}