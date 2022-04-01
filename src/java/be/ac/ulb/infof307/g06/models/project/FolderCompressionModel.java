package be.ac.ulb.infof307.g06.models.project;

import be.ac.ulb.infof307.g06.exceptions.CompressionException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Allows the compression to tar.gz files
 */
public final class FolderCompressionModel {

    public FolderCompressionModel() {
    }

    /**
     * Compresses the file
     * This method creates the archive .tar, and adds every argument files specified
     * in parameter
     * @param compressFilePath the path to the generated .tar archive
     * @param files            the files to compress
     * @throws CompressionException
     */
    public static void compress(String compressFilePath, List<File> files) throws CompressionException {
        try {
            TarArchiveOutputStream out = getTarArchiveOutputStreamFromFileWithName(compressFilePath);
            for (File file : files) {
                addToCompress(out, file, "");
            }
            out.close();
        } catch (IOException e) {
            throw new CompressionException(e);
        }
    }

    /**
     * Gives the .tar archive where we can put files in
     * @param name of the archive
     * @return the archive in TarArchiveOutputStream format
     * @throws IOException if unable create the archive
     */
    private static TarArchiveOutputStream getTarArchiveOutputStreamFromFileWithName(String name) throws IOException {
        TarArchiveOutputStream out = new TarArchiveOutputStream(new GzipCompressorOutputStream(new FileOutputStream(name)));
        out.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        return out;
    }

    /**
     * Adds the given file to the TarArchiveOutputStream .tar archive.
     * If the given file is a directory, adds all the elements in it.
     * @param tarArchive          the .tar destination archive
     * @param file                to add to the archive
     * @param sourceFileDirectory of the given file
     * @throws IOException if unable to reach the given file, or the .tar archive
     */
    private static void addToCompress(TarArchiveOutputStream tarArchive, File file, String sourceFileDirectory) throws IOException {
        String entry = sourceFileDirectory + File.separator + file.getName();
        if (sourceFileDirectory.isEmpty()) {
            entry = file.getName();
        }
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    addToCompress(tarArchive, child, entry);
                }
            }
        } else if (file.isFile()) {
            tarArchive.putArchiveEntry(new TarArchiveEntry(file, entry));
            FileInputStream in = new FileInputStream(file);
            IOUtils.copy(in, tarArchive);
            tarArchive.closeArchiveEntry();
        }
    }
}
