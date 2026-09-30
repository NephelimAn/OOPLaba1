import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Environment {

    public static final Random RANDOM = new Random();

    private final int width;
    private final int height;

    private final Agent[][] grid;

    private int turn;

    public Environment(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Agent[height][width];
        this.turn = 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTurn() {
        return turn;
    }

    public Agent getAgent(int x, int y) {
        if (!isInside(x, y)) {
            return null;
        }

        return grid[y][x];
    }

    public boolean isInside(int x, int y) {
        return x >= 0 &&
                x < width &&
                y >= 0 &&
                y < height;
    }

    public boolean addAgent(Agent agent) {

        int x = agent.getX();
        int y = agent.getY();

        if (!isInside(x, y)) {
            return false;
        }

        if (grid[y][x] != null) {
            return false;
        }

        grid[y][x] = agent;

        return true;
    }

    public void removeAgent(Agent agent) {

        int x = agent.getX();
        int y = agent.getY();

        if (isInside(x, y) && grid[y][x] == agent) {
            grid[y][x] = null;
        }
    }

    public boolean moveAgent(Agent agent, int newX, int newY) {

        if (!isInside(newX, newY)) {
            return false;
        }

        if (grid[newY][newX] != null) {
            return false;
        }

        int oldX = agent.getX();
        int oldY = agent.getY();

        if (grid[oldY][oldX] != agent) {
            return false;
        }

        grid[oldY][oldX] = null;
        grid[newY][newX] = agent;

        agent.setPosition(newX, newY);

        return true;
    }
    public List<Agent> getAgents() {

        List<Agent> agents = new ArrayList<>();

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                if (grid[y][x] != null) {
                    agents.add(grid[y][x]);
                }
            }
        }

        return agents;
    }


    public List<Agent> getAgentsInArea(
            int centerX,
            int centerY,
            int radius
    ) {

        List<Agent> agents = new ArrayList<>();

        for (int y = centerY - radius;
             y <= centerY + radius;
             y++) {

            for (int x = centerX - radius;
                 x <= centerX + radius;
                 x++) {

                if (!isInside(x, y)) {
                    continue;
                }

                Agent agent = grid[y][x];

                if (agent != null &&
                        !(agent.getX() == centerX &&
                                agent.getY() == centerY)) {

                    agents.add(agent);
                }
            }
        }

        return agents;
    }
    public List<int[]> getEmptyNeighbors(
            int centerX,
            int centerY,
            int radius
    ) {

        List<int[]> emptyCells = new ArrayList<>();

        // Вверх
        if (isInside(centerX, centerY - 1)
                && getAgent(centerX, centerY - 1) == null) {

            emptyCells.add(new int[]{
                    centerX,
                    centerY - 1
            });
        }

        // Вниз
        if (isInside(centerX, centerY + 1)
                && getAgent(centerX, centerY + 1) == null) {

            emptyCells.add(new int[]{
                    centerX,
                    centerY + 1
            });
        }

        // Влево
        if (isInside(centerX - 1, centerY)
                && getAgent(centerX - 1, centerY) == null) {

            emptyCells.add(new int[]{
                    centerX - 1,
                    centerY
            });
        }

        // Вправо
        if (isInside(centerX + 1, centerY)
                && getAgent(centerX + 1, centerY) == null) {

            emptyCells.add(new int[]{
                    centerX + 1,
                    centerY
            });
        }

        return emptyCells;
    }
    public void nextTurn() {

        turn++;

        // Создаём копию списка агентов
        List<Agent> agents =
                new ArrayList<>(getAgents());

        for (Agent agent : agents) {

            // Агент мог быть съеден другим агентом
            if (!containsAgent(agent)) {
                continue;
            }

            // Агент совершает своё действие
            agent.act(this);

            // Если закончилась энергия - удаляем
            if (agent.isDead()) {
                removeAgent(agent);
            }
        }
    }

    private boolean containsAgent(Agent agent) {

        int x = agent.getX();
        int y = agent.getY();

        return isInside(x, y)
                && grid[y][x] == agent;
    }
    public boolean hasAllAgentTypes() {

        boolean hasPlants = false;
        boolean hasHerbivores = false;
        boolean hasPredators = false;

        for (Agent agent : getAgents()) {

            if (agent instanceof Plant) {

                hasPlants = true;

            } else if (agent instanceof Herbivore) {

                hasHerbivores = true;

            } else if (agent instanceof Predator) {

                hasPredators = true;
            }
        }

        return hasPlants
                && hasHerbivores
                && hasPredators;
    }
    public boolean isEmpty() {
        return getAgents().isEmpty();
    }
}