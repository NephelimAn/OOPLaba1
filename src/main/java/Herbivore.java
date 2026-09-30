import java.util.List;

public class Herbivore extends Agent {

    private static final int INITIAL_ENERGY = 30;
    private static final int ENERGY_LOSS_PER_TURN = 3;
    private static final int REPRODUCTION_THRESHOLD = 60;
    private static final int VISION_RADIUS = 2;

    public Herbivore(int x, int y) {
        super(x, y, INITIAL_ENERGY);
    }

    @Override
    public void act(Environment environment) {

        // Каждый ход теряем энергию
        energy -= ENERGY_LOSS_PER_TURN;

        if (isDead()) {
            return;
        }

        // Сначала ищем хищника
        Predator predator = findNearestPredator(environment);

        if (predator != null) {

            // Если увидели хищника - убегаем
            moveAwayFrom(
                    environment,
                    predator.getX(),
                    predator.getY()
            );

        } else {

            // Если хищника нет - ищем растение
            Plant plant = findNearestPlant(environment);

            if (plant != null) {

                int distance = distanceTo(
                        plant.getX(),
                        plant.getY()
                );

                // Если растение рядом - съедаем
                if (distance == 1) {
                    eat(environment, plant);
                } else {

                    // Иначе идём в его сторону
                    moveTowards(
                            environment,
                            plant.getX(),
                            plant.getY()
                    );
                }

            } else {

                // Если ничего интересного не нашли -
                // двигаемся случайно
                moveRandomly(environment);
            }
        }

        // Проверяем возможность размножения
        if (energy >= REPRODUCTION_THRESHOLD) {
            reproduce(environment);
        }
    }

    private Plant findNearestPlant(Environment environment) {

        List<Agent> agents =
                environment.getAgentsInArea(
                        x,
                        y,
                        VISION_RADIUS
                );

        Plant nearest = null;
        int minDistance = Integer.MAX_VALUE;

        for (Agent agent : agents) {

            if (agent instanceof Plant) {

                int distance = distanceTo(
                        agent.getX(),
                        agent.getY()
                );

                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = (Plant) agent;
                }
            }
        }

        return nearest;
    }

    private Predator findNearestPredator(Environment environment) {

        List<Agent> agents =
                environment.getAgentsInArea(
                        x,
                        y,
                        VISION_RADIUS
                );

        Predator nearest = null;
        int minDistance = Integer.MAX_VALUE;

        for (Agent agent : agents) {

            if (agent instanceof Predator) {

                int distance = distanceTo(
                        agent.getX(),
                        agent.getY()
                );

                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = (Predator) agent;
                }
            }
        }

        return nearest;
    }

    private int distanceTo(int targetX, int targetY) {

        return Math.abs(x - targetX)
                + Math.abs(y - targetY);
    }

    private void moveTowards(
            Environment environment,
            int targetX,
            int targetY
    ) {

        int newX = x;
        int newY = y;

        if (Math.abs(targetX - x)
                > Math.abs(targetY - y)) {

            if (targetX > x) {
                newX++;
            } else if (targetX < x) {
                newX--;
            }

        } else {

            if (targetY > y) {
                newY++;
            } else if (targetY < y) {
                newY--;
            }
        }

        environment.moveAgent(
                this,
                newX,
                newY
        );
    }

    private void moveAwayFrom(
            Environment environment,
            int targetX,
            int targetY
    ) {

        int newX = x;
        int newY = y;

        if (Math.abs(targetX - x)
                > Math.abs(targetY - y)) {

            if (targetX > x) {
                newX--;
            } else if (targetX < x) {
                newX++;
            }

        } else {

            if (targetY > y) {
                newY--;
            } else if (targetY < y) {
                newY++;
            }
        }

        environment.moveAgent(
                this,
                newX,
                newY
        );
    }

    private void moveRandomly(Environment environment) {

        int[][] directions = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1},
                {0, 0}
        };

        int[] direction =
                directions[
                        Environment.RANDOM.nextInt(
                                directions.length
                        )
                        ];

        environment.moveAgent(
                this,
                x + direction[0],
                y + direction[1]
        );
    }

    private void eat(
            Environment environment,
            Plant plant
    ) {

        int plantX = plant.getX();
        int plantY = plant.getY();

        // Удаляем растение
        environment.removeAgent(plant);

        // Занимаем клетку съеденного растения
        environment.moveAgent(
                this,
                plantX,
                plantY
        );

        // Получаем его энергию
        energy += plant.getEnergy();
    }

    private void reproduce(Environment environment) {

        List<int[]> emptyCells =
                environment.getEmptyNeighbors(
                        x,
                        y,
                        1
                );

        if (!emptyCells.isEmpty()) {

            int[] position =
                    emptyCells.get(
                            Environment.RANDOM.nextInt(
                                    emptyCells.size()
                            )
                    );

            Herbivore child =
                    new Herbivore(
                            position[0],
                            position[1]
                    );

            environment.addAgent(child);

            energy -= INITIAL_ENERGY;
        }
    }

    @Override
    public char getSymbol() {
        return 'Т';
    }
}