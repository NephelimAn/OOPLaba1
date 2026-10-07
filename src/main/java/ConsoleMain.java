import java.util.Scanner;

public class ConsoleMain {

    private static final int WIDTH = 40;
    private static final int HEIGHT = 25;

    private static final int INITIAL_PLANTS = 375;
    private static final int INITIAL_HERBIVORES = 40;
    private static final int INITIAL_PREDATORS = 12;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Создаём среду
        Environment environment =
                new Environment(WIDTH, HEIGHT);

        // Создаём начальные популяции
        initializeEnvironment(environment);

        System.out.println("==============================");
        System.out.println("     ИСКУССТВЕННАЯ ЖИЗНЬ");
        System.out.println("==============================");
        System.out.println("Р - растение");
        System.out.println("Т - травоядное");
        System.out.println("Х - хищник");
        System.out.println(". - пустая клетка");
        System.out.println();

        // Показываем начальное состояние
        printField(environment);

        System.out.print("\nВведите количество ходов: ");

        int turns = scanner.nextInt();

        // Убираем оставшийся после nextInt() Enter
        scanner.nextLine();

        // ==========================================
        // Основной цикл симуляции
        // ==========================================

        for (int i = 0; i < turns; i++) {

            // Если какой-либо вид уже исчез
            if (!environment.hasAllAgentTypes()) {
                break;
            }

            System.out.println();
            System.out.println(
                    "Нажмите Enter для следующего хода..."
            );

            scanner.nextLine();

            // Выполняем один ход
            environment.nextTurn();

            // Показываем новое положение агентов
            printField(environment);

            // Если после хода исчез один из видов
            if (!environment.hasAllAgentTypes()) {
                break;
            }
        }

        // ==========================================
        // Результат
        // ==========================================

        System.out.println();
        System.out.println("==============================");

        if (!environment.hasAllAgentTypes()) {

            System.out.println(
                    "Симуляция завершена: один из видов исчез."
            );

            printExtinctSpecies(environment);

        } else {

            System.out.println(
                    "Выполнено заданное количество ходов."
            );
        }

        System.out.println("==============================");

        scanner.close();
    }

    // ==========================================
    // Вывод игрового поля
    // ==========================================

    private static void printField(
            Environment environment
    ) {

        System.out.println();
        System.out.println(
                "ХОД: " + environment.getTurn()
        );

        // Верхняя граница
        System.out.print("   ");

        for (int x = 0;
             x < environment.getWidth();
             x++) {

            System.out.print("--");
        }

        System.out.println();

        // Само поле
        for (int y = 0;
             y < environment.getHeight();
             y++) {

            System.out.printf("%2d ", y);

            for (int x = 0;
                 x < environment.getWidth();
                 x++) {

                Agent agent =
                        environment.getAgent(x, y);

                if (agent == null) {

                    System.out.print(". ");

                } else {

                    System.out.print(
                            agent.getSymbol() + " "
                    );
                }
            }

            System.out.println();
        }

        // Нижняя граница
        System.out.print("   ");

        for (int x = 0;
             x < environment.getWidth();
             x++) {

            System.out.print("--");
        }

        System.out.println();

        // Статистика
        printStatistics(environment);
    }

    // ==========================================
    // Вывод статистики
    // ==========================================

    private static void printStatistics(
            Environment environment
    ) {

        int plants = 0;
        int herbivores = 0;
        int predators = 0;

        for (Agent agent :
                environment.getAgents()) {

            if (agent instanceof Plant) {

                plants++;

            } else if (agent instanceof Herbivore) {

                herbivores++;

            } else if (agent instanceof Predator) {

                predators++;
            }
        }

        System.out.println();

        System.out.println(
                "Растения: " + plants
                        + " | Травоядные: " + herbivores
                        + " | Хищники: " + predators
        );
    }

    // ==========================================
    // Определение исчезнувшего вида
    // ==========================================

    private static void printExtinctSpecies(
            Environment environment
    ) {

        boolean plants = false;
        boolean herbivores = false;
        boolean predators = false;

        for (Agent agent :
                environment.getAgents()) {

            if (agent instanceof Plant) {

                plants = true;

            } else if (agent instanceof Herbivore) {

                herbivores = true;

            } else if (agent instanceof Predator) {

                predators = true;
            }
        }

        if (!plants) {
            System.out.println(
                    "Исчезнувший вид: растения."
            );
        }

        if (!herbivores) {
            System.out.println(
                    "Исчезнувший вид: травоядные."
            );
        }

        if (!predators) {
            System.out.println(
                    "Исчезнувший вид: хищники."
            );
        }
    }

    // ==========================================
    // Создание начальной среды
    // ==========================================

    private static void initializeEnvironment(
            Environment environment
    ) {

        createPlants(
                environment,
                INITIAL_PLANTS
        );

        createHerbivores(
                environment,
                INITIAL_HERBIVORES
        );

        createPredators(
                environment,
                INITIAL_PREDATORS
        );
    }

    // ==========================================
    // Создание растений
    // ==========================================

    private static void createPlants(
            Environment environment,
            int count
    ) {

        int created = 0;

        while (created < count) {

            int x =
                    Environment.RANDOM.nextInt(
                            environment.getWidth()
                    );

            int y =
                    Environment.RANDOM.nextInt(
                            environment.getHeight()
                    );

            if (environment.getAgent(x, y) == null) {

                environment.addAgent(
                        new Plant(x, y)
                );

                created++;
            }
        }
    }

    // ==========================================
    // Создание травоядных
    // ==========================================

    private static void createHerbivores(
            Environment environment,
            int count
    ) {

        int created = 0;

        while (created < count) {

            int x =
                    Environment.RANDOM.nextInt(
                            environment.getWidth()
                    );

            int y =
                    Environment.RANDOM.nextInt(
                            environment.getHeight()
                    );

            if (environment.getAgent(x, y) == null) {

                environment.addAgent(
                        new Herbivore(x, y)
                );

                created++;
            }
        }
    }

    // ==========================================
    // Создание хищников
    // ==========================================

    private static void createPredators(
            Environment environment,
            int count
    ) {

        int created = 0;

        while (created < count) {

            int x =
                    Environment.RANDOM.nextInt(
                            environment.getWidth()
                    );

            int y =
                    Environment.RANDOM.nextInt(
                            environment.getHeight()
                    );

            if (environment.getAgent(x, y) == null) {

                environment.addAgent(
                        new Predator(x, y)
                );

                created++;
            }
        }
    }
}