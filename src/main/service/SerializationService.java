package service;

import model.Horse;
import model.Stable;

import java.io.*;
import java.util.List;

public class SerializationService {

    public void saveStable(Stable stable, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(stable);
        }
    }

    public Stable loadStable(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Stable) ois.readObject();
        }
    }

    public void saveHorse(Horse horse, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(horse);
        }
    }

    public Horse loadHorse(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Horse) ois.readObject();
        }
    }

    public void saveStables(List<Stable> stables, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(stables);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Stable> loadStables(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Stable>) ois.readObject();
        }
    }

    public void saveHorses(List<Horse> horses, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(horses);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Horse> loadHorses(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Horse>) ois.readObject();
        }
    }
}
