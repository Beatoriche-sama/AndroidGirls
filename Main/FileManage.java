package Main;

import java.io.*;
import java.util.*;

public class FileManage {

    public static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        directory.delete();
    }

    private static void objectWrite(Object object, ObjectOutputStream oos)
            throws IOException {
        oos.writeObject(object);
        oos.flush();
    }

    public static ArrayList<Object> objectsLoad(String fileName) throws IOException, ClassNotFoundException {
        ArrayList<Object> objects = new ArrayList<>();

        File f = new File(fileName);
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream input = new ObjectInputStream(fis);

        boolean cont = true;
        while (cont) {
            try {
                Object obj = input.readObject();
                if (obj != null) {
                    objects.add(obj);
                } else {
                    cont = false;
                }
            } catch (EOFException e) {
                break;
            }
        }
        input.close();
        fis.close();

        return objects;
    }

    public static Object objectLoad(String fileName)
            throws IOException, ClassNotFoundException {
        File f = new File(fileName);
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream input = new ObjectInputStream(fis);
        Object object = input.readObject();
        input.close();
        fis.close();
        return object;
    }

    public static void mapSave(String fileName, Map<?, ?> map) throws IOException {
        File f = new File(fileName);
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        objectWrite(map, oos);
        oos.close();
        fos.close();

    }

    public static Map<Object, Object> mapLoad(String fileName) throws IOException {
        File f = new File(fileName);
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream input = new ObjectInputStream(fis);
        Map<Object, Object> map = null;
        boolean cont = true;
        while (cont) {
            try {
                map = (Map<Object, Object>) input.readObject();
                if (map == null) cont = false;
            } catch (EOFException | ClassNotFoundException e) {
                break;
            }
        }
        input.close();
        fis.close();

        return map;
    }

}
