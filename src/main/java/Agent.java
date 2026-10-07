public abstract class Agent {
    protected int x;
    protected int y;
    protected int energy;

    public Agent(int x, int y, int energy) {
        this.x = x;
        this.y = y;
        this.energy = energy;
    }

    public abstract void act(Environment environment);

    public abstract char getSymbol();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getEnergy() {
        return energy;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addEnergy(int amount) {
        energy += amount;
    }

    public void removeEnergy(int amount) {
        energy -= amount;
    }

    public boolean isDead() {
        return energy <= 0;
    }
}