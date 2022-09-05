import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    private static final String BASEDIR = "/home/anonymous/Games/savegames/";
    private static final int BUFFER_SIZE = 1024;

    public static void openZip(String archivePath, String savegamesPath) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(archivePath))) {
            ZipEntry zentry;
            while ((zentry = zis.getNextEntry()) != null) {
                Path path = Paths.get(zentry.getName());
                String fileName = path.getFileName().toString();
                byte[] buffer = new byte[BUFFER_SIZE];
                try (FileOutputStream fos = new FileOutputStream(savegamesPath + fileName)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GameProgress openProgress(String path) {
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (GameProgress) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        openZip(BASEDIR + "savegames.zip", BASEDIR);
        System.out.println(openProgress(BASEDIR + "save0.dat"));
    }
}