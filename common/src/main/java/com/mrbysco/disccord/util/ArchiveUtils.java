package com.mrbysco.disccord.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Based on ArchiveUtils from MehVahdJukaar/cameramod
 * <a href="https://github.com/MehVahdJukaar/cameramod/blob/master/common/src/main/java/net/mehvahdjukaar/vista/client/web/files/ArchiveUtils.java">...</a>
 */
public final class ArchiveUtils {

	public static void extract(Path archive, Path destination) throws IOException, InterruptedException {
		String name = archive.getFileName().toString().toLowerCase(Locale.ROOT);

		if (isZip(name)) {
			extractZip(archive, destination);
			return;
		}

		if (isTarFamily(name)) {
			extractTar(archive, destination);
			return;
		}

		throw new IOException("Unsupported archive format: " + archive.getFileName());
	}

	private static void extractTar(Path archive, Path destination) throws IOException, InterruptedException {
		Process p = new ProcessBuilder(
				"tar", "-xf", archive.toAbsolutePath().toString(),
				"-C", destination.toAbsolutePath().toString()
		).start();

		if (p.waitFor() != 0) {
			throw new IOException("Tar extraction failed for " + archive.getFileName());
		}
	}

	private static void extractZip(Path archive, Path destination) throws IOException {
		Path absDestination = destination.toAbsolutePath().normalize();
		try (ZipFile zf = new ZipFile(archive.toFile())) {
			Enumeration<? extends ZipEntry> entries = zf.entries();
			while (entries.hasMoreElements()) {
				ZipEntry e = entries.nextElement();

				Path out = absDestination.resolve(e.getName()).normalize();
				if (!out.startsWith(absDestination)) {
					throw new IOException("Zip entry escapes destination: " + e.getName());
				}

				if (e.isDirectory()) {
					Files.createDirectories(out);
					continue;
				}

				Files.createDirectories(out.getParent());
				try (InputStream is = zf.getInputStream(e)) {
					Files.copy(is, out, StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
	}

	private static boolean isZip(String fileName) {
		return fileName.endsWith(".zip");
	}

	private static boolean isTarFamily(String fileName) {
		return fileName.endsWith(".tar")
				|| fileName.endsWith(".tar.gz")
				|| fileName.endsWith(".tgz")
				|| fileName.endsWith(".tar.xz")
				|| fileName.endsWith(".txz")
				|| fileName.endsWith(".tar.bz2")
				|| fileName.endsWith(".tbz2");
	}

}