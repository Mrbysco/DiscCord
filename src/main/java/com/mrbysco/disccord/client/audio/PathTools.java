package com.mrbysco.disccord.client.audio;

import org.apache.commons.lang3.SystemUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * PathTools
 */
public class PathTools {

	/**
	 * Traverse the PATH to find an executable
	 * @param filename The name of the executable to find
	 * @return The path of the executable if it's found or Optional.empty
	 */
	public static Optional<String> traversePath(String filename) {
		String separator = SystemUtils.IS_OS_UNIX ? ":" : ";";

		String path = System.getenv("PATH");
		if (path == null) {
			return Optional.empty();
		}

		String[] dirs = path.split(separator);
		for (String dir : dirs) {
			Path executablePath = Paths.get(dir, filename);
			if (executablePath.toFile().exists()) {
				return Optional.of(executablePath.toString());
			}
		}

		return Optional.empty();
	}
}
