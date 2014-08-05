package openloco.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class LwjglNativeLoader {

    public static final Logger LOGGER = LoggerFactory.getLogger(LwjglNativeLoader.class);

    public static enum Platform {
        OSX("liblwjgl.jnilib", "openal.dylib"),
        LINUX_32("liblwjgl.so", "libopenal.so"),
        LINUX_64("liblwjgl64.so", "libopenal64.so"),
        WINDOWS_32("lwjgl.dll", "OpenAL32.dll"),
        WINDOWS_64("lwjgl64.dll", "OpenAL64.dll");

        private final String lwjglLibrary;
        private final String openAlLibrary;

        private Platform(String lwjglLibrary, String openAlLibrary) {
            this.lwjglLibrary = lwjglLibrary;
            this.openAlLibrary = openAlLibrary;
        }

        public String getLwjglLibrary() {
            return lwjglLibrary;
        }

        public String getOpenAlLibrary() {
            return openAlLibrary;
        }
    }

    public void loadLibraries() {
        Platform platform = determinePlatform();
        loadLibrariesForPlatform(platform);
    }

    private void loadLibrariesForPlatform(Platform platform) {
        loadLibrary(platform.getLwjglLibrary(), "lwjgl");
        loadLibrary(platform.getOpenAlLibrary(), "openal");
    }

    private void loadLibrary(String library, String name) {
        File loadedPath;
        try {
            loadedPath = extractLibrary(library, name);
            System.setProperty("org.lwjgl.librarypath", loadedPath.getParentFile().getAbsolutePath());
        } catch (IOException e) {
            LOGGER.warn("Could not find library " + library +
                    " as resource, trying fallback lookup through System.loadLibrary");
            System.loadLibrary(library);
        }
    }

    private Platform determinePlatform() {
        String osArch = System.getProperty("os.arch");
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.startsWith("linux")) {
            if (osArch.endsWith("64")) {
                return Platform.LINUX_64;
            }
            else {
                return Platform.LINUX_32;
            }
        }
        else if (osName.startsWith("win")) {
            if (osArch.equalsIgnoreCase("x86")) {
                return Platform.WINDOWS_32;
            }
            else {
                return Platform.WINDOWS_64;
            }
        }
        else if (osName.startsWith("mac os x")) {
            return Platform.OSX;
        }
        else {
            throw new RuntimeException("Unsupported platform: " + osName + "/" + osArch);
        }
    }



    private File extractLibrary(String library, String name) throws IOException {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = this.getClass().getClassLoader().getResourceAsStream(library);
            String tmpDirName = System.getProperty("java.io.tmpdir");
            File tmpDir = new File(tmpDirName);
            if (!tmpDir.exists()) {
                tmpDir.mkdir();
            }
            File file = new File(tmpDir, System.mapLibraryName(name));
            file.deleteOnExit();
            out = new FileOutputStream(file);

            int cnt;
            byte buf[] = new byte[16 * 1024];
            // copy until done.
            while ((cnt = in.read(buf)) >= 1) {
                out.write(buf, 0, cnt);
            }
            LOGGER.info("Saved libfile: " + file.getAbsoluteFile());
            return file;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignore) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
