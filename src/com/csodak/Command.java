

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

@Deprecated
public class Command {
    ArrayList<Asteroid> asteroids = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Robot> robots = new ArrayList<>();
    ArrayList<Ufo> ufos = new ArrayList<>();

    private static Command instance;

    private Command() {

    }

    public static Command GetInstance() {
        if (instance == null) {
            instance = new Command();
        }
        return instance;
    }

    private void cls() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


    private Resource ResourceFromString(String resourceName) {
        switch (resourceName.toLowerCase()) {
            case "coal": {
                return new Coal();
            }
            case "iron": {
                return new Iron();
            }
            case "ice": {
                return new Ice();
            }
            case "uranium": {
                return new Uranium();
            }
            default:
                throw new IllegalArgumentException();
        }
    }

    public void HandleCommands() throws IOException, IndexOutOfBoundsException {
        try (var br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                try {
                    String cmd = br.readLine();
                    if (cmd == null) {
                        System.out.println("stdin closed, goodbye");
                        System.exit(0);
                    }
                    String[] parts = cmd.split(" ");

                    //Perihelion
                    switch (parts[0]) {
                        case "perihelion": {
                            switch (parts[1]) {
                                case "random": {
                                    int val = parseInt(parts[2]);
                                    if (!(val == 0 || val == 1))
                                        throw new IllegalArgumentException();
                                    Game.GetInstance().SetPerihelionPossibility(val == 1 ? 0.2 : 0.0);
                                    System.out.println("perihelion random set to " + val);
                                    break;
                                }
                                case "set": {
                                    int val = parseInt(parts[2]);
                                    int dur = 1;
                                    try {
                                        dur = parseInt(parts[3]);
                                    } catch (IndexOutOfBoundsException ignored) {
                                    }
                                    if (!(val == 0 || val == 1))
                                        throw new IllegalArgumentException();
                                    Game.GetInstance().PerihelionAll(val != 0, dur);
                                    if (val == 0) {
                                        System.out.println("perihelion set to " + val);
                                    } else {
                                        System.out.println("perihelion set to " + val + " for " + dur + " rounds");
                                    }
                                    break;
                                }
                                default:
                                    throw new IllegalArgumentException();
                            }
                            break;
                        } // perihelion end switch

                        //Solarstorm
                        case "solarstorm": {
                            switch (parts[1]) {
                                case "random": {
                                    int val = parseInt(parts[2]);
                                    if (!(val == 0 || val == 1))
                                        throw new IllegalArgumentException();
                                    Game.GetInstance().SetSolarStormPossibility(val == 1 ? 0.2 : 0.0);
                                    System.out.println("solarstorm random set to " + val);
                                    break;
                                }
                                case "set": {
                                    int val = parseInt(parts[2]);
                                    if (!(val == 0 || val == 1))
                                        throw new IllegalArgumentException();
                                    Game.GetInstance().SolarStormAll(val != 0);
                                    System.out.println("solarstorm set to " + val);
                                    break;
                                }
                                default:
                                    throw new IllegalArgumentException();
                            }
                            break;
                        } // solarstorm switch end

                        //Game
                        case "game": {
                            switch (parts[1]) {
                                case "add":
                                    String obj = parts[2];
                                    int val = 1;
                                    try {
                                        val = parseInt(parts[3]);
                                    }
                                    catch (IndexOutOfBoundsException ignored) {

                                    }
                                    for (int i = 0; i < val; ++i) {
                                        switch (obj) {
                                            case "player":
                                                Player player = new Player(null);
                                                System.out.println(obj + " " + players.size() + " added to game");
                                                players.add(player);
                                                Game.GetInstance().AddEntity(player);
                                                break;
                                            case "robot":
                                                Robot robot = new Robot(null);
                                                System.out.println(obj + " " + robots.size() + " added to game");
                                                robots.add(robot);
                                                Game.GetInstance().AddEntity(robot);
                                                break;
                                            case "ufo":
                                                Ufo ufo = new Ufo(null);
                                                System.out.println(obj + " " + ufos.size() + " added to game");
                                                ufos.add(ufo);
                                                Game.GetInstance().AddEntity(ufo);
                                                break;
                                            case "asteroid":
                                                Effect perihelion = new Effect();
                                                Effect solarstorm = new Effect();
                                                Asteroid asteroid = new Asteroid(perihelion, solarstorm, null, 1);
                                                System.out.println(obj + " " + asteroids.size() + " added to game");
                                                asteroids.add(asteroid);
                                                Game.GetInstance().AddAsteroid(asteroid, perihelion, solarstorm);
                                                break;
                                            default:
                                                throw new IllegalArgumentException();
                                        }
                                    }
                                    break;
                                case "next":
                                    Game.GetInstance().Next();
                                    System.out.println(Game.GetInstance().GetRoundCount() + " round started");
                                    break;
                                default:
                                    throw new IllegalArgumentException();
                            }
                            break;
                        } // game switch end

                        //Asteroid
                        case "asteroid": {
                            int asteroid_id = parseInt(parts[1]);
                            Asteroid asteroid1 = asteroids.get(asteroid_id);
                            if (parts[2].equals("link")) {
                                int id = parseInt(parts[4]);
                                if (asteroid_id == id) {
                                    throw new IllegalArgumentException();
                                }
                                if (asteroid1.GetNeighbours().contains(asteroids.get(id))) {
                                    throw new IllegalArgumentException();
                                }
                                switch (parts[3]) {
                                    case "portal": {
                                        Portal pair[] = Portal.CreatePair();
                                        Asteroid asteroid2 = asteroids.get(id);
                                        pair[0].Link(asteroid1);
                                        pair[1].Link(asteroid2);
                                        System.out.println("portal has been placed between asteroid " + asteroid_id + " and asteroid " + id);
                                        break;
                                    }
                                    case "asteroid": {
                                        Asteroid asteroid2 = asteroids.get(id);
                                        asteroid1.SetNeighbour(asteroid2);
                                        asteroid2.SetNeighbour(asteroid1);
                                        System.out.println("link has been created between asteroid " + asteroid_id + " and asteroid " + id);
                                        break;
                                    }
                                    default:
                                        throw new IllegalArgumentException();
                                }
                            } else if (parts[2].equals("place")) {
                                int id = parseInt(parts[4]);
                                Entity entity;
                                switch (parts[3]) {
                                    case "player": {
                                        entity = players.get(id);
                                        break;
                                    }
                                    case "robot": {
                                        entity = robots.get(id);
                                        break;
                                    }
                                    case "ufo": {
                                        entity = ufos.get(id);
                                        break;
                                    }
                                    default:
                                        throw new IllegalArgumentException();
                                }
                                Asteroid entityLocation = entity.GetLocation();
                                if (entityLocation != null) {
                                    entityLocation.RemoveEntity(entity);
                                }
                                entity.SetLocation(asteroids.get(asteroid_id));
                                System.out.println(parts[3] + " " + id + " has been placed on asteroid " + asteroid_id);

                            } else if (parts[2].equals("crust")) {
                                int size = parseInt(parts[3]);
                                asteroid1.SetCrust(size);
                                System.out.println("asteroid " + asteroid_id + "'s crust has been set to " + size);

                            } else if (parts[2].equals("status")) {
                                asteroids.get(asteroid_id).Status();
                            } else {
                                throw new IllegalArgumentException();
                            }
                            break;
                        } // asteroid switch end

                        //Resource
                        case "resource": {
                            Asteroid asteroid = asteroids.get(parseInt(parts[1]));
                            switch (parts[2]) {
                                case "set": {
                                    asteroid.SetCore(ResourceFromString(parts[3]));
                                    System.out.println("asteroid " + parts[1] + "'s core is set to " + parts[3]);
                                    break;
                                }
                                case "clear": {
                                    asteroid.SetCore(null);
                                    System.out.println("asteroid " + parts[1] + "'s core has been cleared");
                                    break;
                                }
                                default:
                                    throw new IllegalArgumentException();
                            }
                            break;
                        } // resource switch end

                        //Player
                        case "player": {
                            Player player = players.get(parseInt(parts[1]));
                            switch (parts[2]) {
                                case "status": {
                                    player.Status();
                                    break;
                                }
                                case "move": {
                                    System.out.println("player " + parts[1] + " has " + (player.Move(asteroids.get(parseInt(parts[3]))) ? "" : "not ") +
                                            "moved to asteroid " + parts[3]);
                                    break;
                                }
                                case "drill": {
                                    System.out.println("asteroid " + asteroids.indexOf(player.GetLocation()) + "'s crust has " + ((player.Drill())?"" : "not ") + "been drilled");
                                    break;
                                }
                                case "mine": {
                                    Resource r = player.Mine();
                                    System.out.println("asteroid " + asteroids.indexOf(player.GetLocation()) + (r == null ? " cannot be mined" : " has been mined and yielded " + r.toString()));
                                    //player.Mine();
                                    break;
                                }
                                case "craft": {
                                    switch (parts[3]) {
                                        case "robot": {
                                            Robot r = player.CraftRobot();
                                            if (r != null) {
                                                robots.add(r);
                                            }
                                            System.out.println("robot has " + (r != null ? "" : "not ") + "been crafted");
                                            break;
                                        }
                                        case "portal": {
                                            System.out.println("portal has " + ((player.CraftPortal()) ? "" : "not ") + "been crafted");
                                            break;
                                        }
                                        default:
                                            throw new IllegalArgumentException();
                                    }
                                    break;
                                }
                                case "give": {
                                    int count = 1;
                                    try {
                                        count = parseInt(parts[4]);
                                    }
                                    catch (IndexOutOfBoundsException ignore) {

                                    }
                                    int db = 0;
                                    for (int i = 0; i < count; i++) {
                                        if(player.Trade(ResourceFromString(parts[3]))){
                                            db++;
                                        }
                                    }
                                    System.out.println(db + " " + ResourceFromString(parts[3]) + " has been added to player " + parts[1] + "'s inventory");
                                    break;
                                }
                                case "trade": {
                                    boolean b = player.GiveResource(players.get(parseInt(parts[3])), ResourceFromString(parts[4]));
                                    System.out.println(parts[4] + " has " + (b ? "" : "not ") + "been given to player " + parts[3] + "'s inventory");
                                    break;
                                }
                                case "placeportal": {
                                    System.out.println("portal has " + (player.PlacePortal()? "" : "not ") + "been placed to asteroid " + asteroids.indexOf(player.GetLocation()));
                                    break;
                                }
                                case "placeresource": {
                                    boolean b = player.PlaceResource(ResourceFromString(parts[3]));
                                    System.out.println(parts[3] + " has " + (b ? "": "not ") + "been placed into asteroid " + asteroids.indexOf(player.GetLocation()));
                                    break;
                                }
                                case "energy": {
                                    int val = parseInt(parts[3]);
                                    player.SetEnergy(val);
                                    System.out.println("player " + parts[1] + "'s energy set to " + val);
                                    break;
                                }
                                default:
                                    throw new IllegalArgumentException();

                            }
                            break;
                        } //player switch end

                        //Robot
                        case "robot": {
                            Robot lobot = robots.get(parseInt(parts[1]));
                            switch (parts[2]) {
                                case "move": {
                                    System.out.println("robot " + parts[1] + " has " + (lobot.Move(asteroids.get(parseInt(parts[3]))) ? "" : "not ") + "moved to asteroid " + parts[3]);
                                    break;
                                }
                                case "drill": {
                                    System.out.println("asteroid " + asteroids.indexOf(lobot.GetLocation()) + "'s crust has " + ((lobot.Drill())?"" : "not ") + "been drilled");
                                    break;
                                }
                                case "status": {
                                    lobot.Status();
                                    break;
                                }
                                case "energy": {
                                    lobot.SetEnergy(parseInt(parts[3]));
                                    System.out.println("robot " + parts[1] + "'s energy set to " + parts[3]);
                                    break;
                                }
                                default:
                                    throw new IllegalArgumentException();
                            }
                            break;
                        } // robot switch end

                        //Ufo
                        case "ufo": {
                            Ufo ufo = ufos.get(parseInt(parts[1]));
                            switch (parts[2]) {
                                case "move": {
                                    System.out.println("ufo " + parts[1] + " has " + (ufo.Move(asteroids.get(parseInt(parts[3]))) ? "" : "not ") + "moved to asteroid " + parts[3]);
                                    break;
                                }
                                case "steal": {
                                    Resource r = ufo.Steal();
                                    System.out.println("asteroid " + asteroids.indexOf(ufo.GetLocation()) + (r == null ? " cannot be mined" : " has been robbed and yielded " + r.toString()));
                                    break;
                                }
                                case "status": {
                                    ufo.Status();
                                    break;
                                }
                                case "energy": {
                                    ufo.SetEnergy(parseInt(parts[3]));
                                    System.out.println("ufo " + parts[1] + "'s energy set to " + parts[3]);
                                    break;
                                }
                                default:
                                    throw new IllegalArgumentException();
                            }
                            break;
                        }
                    }
                } catch (IndexOutOfBoundsException | IllegalArgumentException ex) {
                    System.out.println("Bad command. Sad panda is sad.");
                }
            }
        }
    }
}
