package be.ac.ulb.infof307.g06.models.project;

import be.ac.ulb.infof307.g06.exceptions.DecompressException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Allows the decompression of a tar.gz files
 */
public class FolderDecompressionModel {
    private TarArchiveInputStream tarArchiveInputStream;

    public FolderDecompressionModel() {
    }

    /**
     * Decompresses a tar.gz file to the directory whose path is the outputPath specified
     * @param tarFileName : path to the tar.gz
     * @param outputPath  : destination of the extracted files
     * @return List of all the files extracted
     * @throws DecompressException
     */
    public List<File> decompress(String tarFileName, String outputPath) throws DecompressException {
        List<File> files = new ArrayList<>();
        try {
            tarArchiveInputStream = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(tarFileName)));
            TarArchiveEntry entry;
            File outputDirectory = new File(outputPath);
            while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {
                if (!entry.isDirectory()) {
                    File file = extractTarEntry(entry, outputDirectory);
                    files.add(file);
                }
            }
            tarArchiveInputStream.close();
        } catch (IOException e) {
            throw new DecompressException(e);
        }
        return files;
    }

    /**
     * Tries to extract all the files contained in the given .tar archive entry
     * @param entry           of the .tar archive
     * @param outputDirectory to extract the filers
     * @throws IOException IO
     * @return the file contained in the .tar archive.
     */
    private File extractTarEntry(TarArchiveEntry entry, File outputDirectory) throws IOException {
        File currentFile = new File(outputDirectory, entry.getName());
        File parent = currentFile.getParentFile();
        if (!parent.exists() && !parent.mkdirs())
            throw new AssertionError("An error occurred while trying to reach the given output path: " + outputDirectory.getAbsolutePath());
        FileOutputStream output = new FileOutputStream(currentFile);
        IOUtils.copy(tarArchiveInputStream, output);
        output.close();
        return currentFile;
    }

}
