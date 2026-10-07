import java.util.List;

public class Predator extends Agent {

    private static final int INITIAL_ENERGY = 300;
    private static final int ENERGY_LOSS_PER_TURN = 2;
    private static final int REPRODUCTION_THRESHOLD = 600;
    private static final int VISION_RADIUS = 2;

    public Predator(int x, int y) {
        super(x, y, INITIAL_ENERGY);
    }

    @Override
    public void act(Environment environment) {

        // Каждый ход теряем энергию
        energy -= ENERGY_LOSS_PER_TURN;

        if (isDead()) {
            return;
        }

        // Ищем ближайшее травоядное
        Herbivore herbivore =
                findNearestHerbivore(environment);

        if (herbivore != null) {

            int distance =
                    distanceTo(
                            herbivore.getX(),
                            herbivore.getY()
                    );

            // Если добыча рядом - съедаем её
            if (distance == 1) {

                eat(
                        environment,
                        herbivore
                );

            } else {

                // Иначе идём к добыче
                moveTowards(
                        environment,
                        herbivore.getX(),
                        herbivore.getY()
                );
            }

        } else {

            // Добычи рядом нет
            moveRandomly(environment);
        }

        // Проверяем размножение
        if (energy >= REPRODUCTION_THRESHOLD) {
            reproduce(environment);
        }
    }

    private Herbivore findNearestHerbivore(
            Environment environment
    ) {

        List<Agent> agents =
                environment.getAgentsInArea(
                        x,
                        y,
                        VISION_RADIUS
                );

        Herbivore nearest = null;
        int minDistance = Integer.MAX_VALUE;

        for (Agent agent : agents) {

            if (agent instanceof Herbivore) {

                int distance =
                        distanceTo(
                                agent.getX(),
                                agent.getY()
                        );

                if (distance < minDistance) {

                    minDistance = distance;
                    nearest = (Herbivore) agent;
                }
            }
        }

        return nearest;
    }

    private int distanceTo(
            int targetX,
            int targetY
    ) {

        return Math.abs(x - targetX)
                + Math.abs(y - targetY);
    }

    private void moveTowards(
            Environment environment,
            int targetX,
            int targetY
    ) {

        // Все возможные направления движения
        int[][] directions = {
                {1, 0},   // вправо
                {-1, 0},  // влево
                {0, 1},   // вниз
                {0, -1}   // вверх
        };

        int bestX = x;
        int bestY = y;

        int bestDistance = Integer.MAX_VALUE;

        for (int[] direction : directions) {

            int newX = x + direction[0];
            int newY = y + direction[1];

            // Не выходим за границы поля
            if (!environment.isInside(newX, newY)) {
                continue;
            }

            // Клетка должна быть свободна
            if (environment.getAgent(newX, newY) != null) {
                continue;
            }

            // Считаем расстояние от новой клетки до цели
            int distance =
                    Math.abs(newX - targetX)
                            + Math.abs(newY - targetY);

            // Выбираем клетку, которая ближе всего к цели
            if (distance < bestDistance) {
                bestDistance = distance;
                bestX = newX;
                bestY = newY;
            }
        }

        // Если нашли подходящую клетку - перемещаемся
        if (bestX != x || bestY != y) {
            environment.moveAgent(
                    this,
                    bestX,
                    bestY
            );
        }
    }

    private void moveRandomly(
            Environment environment
    ) {

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
            Herbivore herbivore
    ) {

        int herbivoreX = herbivore.getX();
        int herbivoreY = herbivore.getY();

        // Удаляем съеденное травоядное
        environment.removeAgent(herbivore);

        // Хищник занимает его клетку
        environment.moveAgent(
                this,
                herbivoreX,
                herbivoreY
        );

        // И получает его энергию
        energy += herbivore.getEnergy();
    }

    private void reproduce(
            Environment environment
    ) {

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

            Predator child =
                    new Predator(
                            position[0],
                            position[1]
                    );

            environment.addAgent(child);

            energy -= INITIAL_ENERGY;
        }
    }

    @Override
    public char getSymbol() {
        return 'Х';
    }
}