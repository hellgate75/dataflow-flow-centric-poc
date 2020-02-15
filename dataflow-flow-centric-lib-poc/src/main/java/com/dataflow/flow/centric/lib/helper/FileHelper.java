package com.dataflow.flow.centric.lib.helper;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.core.lib.logger.VlfLogger.Category;
import com.dataflow.core.lib.logger.VlfLogger.LogLevel;
import com.dataflow.core.lib.logger.VlfLogger.Severity;
import com.dataflow.flow.centric.lib.constants.GlobalConstants;
import com.dataflow.flow.centric.lib.stream.listener.CompletionConsumer;
import com.google.common.base.Splitter;

/**
 * FileHelper class to writes the data and the error message to the respective
 * destination error file. If file present, Data will be appended at the end. If
 * file not present, File will be created and data will be written to it.
 * 
 * @author kpu37is
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * 
 */
@Component
public class FileHelper {

	@Autowired
	private VlfLogger vlfLogger;

	@Value("${dataflow.app.environment}")
	private String environment;
	
	public static final int DEFAULT_BUFFER_SIZE = 262144;

	/**
	 * Write the data to the given fileName.
	 * 
	 * @param bytes
	 *            the bytes of the record
	 * @param fileName
	 *            the error file name
	 */
	public void writeToFile(byte[] bytes, String destinationFolder, String fileName) {
		try {
			writeFile(bytes, destinationFolder, fileName);
		} catch (IOException e) {
			this.vlfLogger.write(GlobalConstants.LOG_OPERATION, Category.SYSTEM_ERROR.name(), Severity.ERROR,
					String.format("FileHelper::writeFile: %s -> %s", fileName, e.getMessage()), LogLevel.ERROR);
			this.vlfLogger.write("FileHelper::writeFile", Severity.ERROR, e);
		}
	}

	/**
	 * writeFile method
	 * 
	 * @param bytes
	 * @param destination
	 * @param fileName
	 * @throws IOException
	 */
	public void writeFile(byte[] bytes, String destination, String fileName) throws IOException {
		Files.createDirectories(Paths.get(destination));
		Path destinationPath = Paths.get(destination + getFileSeparator() + fileName);
		Set<StandardOpenOption> options = new HashSet<>(0);
		options.add(StandardOpenOption.CREATE);
		options.add(StandardOpenOption.APPEND);
		try ( FileChannel fileChannel = FileChannel.open(destinationPath, options) ) {
			fileChannel.write(ByteBuffer.wrap(bytes));
			fileChannel.write(ByteBuffer.wrap(System.lineSeparator().getBytes()));
		} catch (Exception e) {
			this.vlfLogger.write(GlobalConstants.LOG_OPERATION, Category.SYSTEM_ERROR.name(), Severity.ERROR,
					"FileHelper::writeFile: " + e.getMessage(), LogLevel.ERROR);
			this.vlfLogger.write("FileHelper::writeFile", Severity.ERROR, e);
		}
	}

	/**
	 * Gets File separator
	 * 
	 * @return
	 */
	public static String getFileSeparator() {
		return FileSystems.getDefault().getSeparator();
	}

	public static String getFileSeparator(String filePath) {
		String separator = FileSystems.getDefault().getSeparator();
		if (filePath != null && !filePath.isEmpty() && filePath.indexOf(separator) < 0) {
			separator = "/";
		}
		return separator;
	}

	/**
	 * moveFile method to move file to destination folder
	 * 
	 * @param filePath
	 * @param destinationFolder
	 * @return Destination Path
	 */
	public String moveFile(String filePath, String destinationFolder) {
		String destinationPathStr = null;
		long sourceSize = -1;
		long destinationSize = -1;
		try {
			Path sourcePath = Paths.get(filePath);

			sourceSize = Files.readAttributes(sourcePath, BasicFileAttributes.class).size();
			this.vlfLogger.write(GlobalConstants.LOG_OPERATION, Category.SUCCESS.name(), Severity.INFO,
					"FileHelper::moveFile:sourcePath " + sourcePath + " size: " + sourceSize, LogLevel.ERROR);
			String separator = getFileSeparator();
			if (filePath.indexOf(separator) < 0) {
				separator = "/";
			}
			String fileName = filePath.substring(filePath.lastIndexOf(separator) + 1);
			Files.createDirectories(Paths.get(destinationFolder));
			Path destinationPath = Paths.get(destinationFolder + separator + fileName);
			Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
			destinationPathStr = destinationPath.toString();
			do {
				destinationSize = Files.readAttributes(destinationPath, BasicFileAttributes.class).size();
				this.vlfLogger.write(GlobalConstants.LOG_OPERATION, Category.SUCCESS.name(), Severity.INFO,
						"FileHelper::moveFile:destinationPath " + destinationPath + " size: " + destinationSize,
						LogLevel.ERROR);
			} while (sourceSize != destinationSize);

		} catch (IOException e) {

			this.vlfLogger.write(GlobalConstants.LOG_OPERATION, Category.SYSTEM_ERROR.name(), Severity.ERROR,
					"FileHelper::moveFile: " + e.getMessage(), LogLevel.ERROR);
			this.vlfLogger.write("FileHelper::moveFile", Severity.ERROR, e);

		}
		return destinationPathStr;
	}

	/**
	 * get current date
	 * 
	 * @return
	 */
	public String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	/**
	 * 
	 * findFiles- Find files in a directory
	 * 
	 * @param directory
	 * @return
	 */
	public List<String> findFiles(String directory) {
		List<String> fileList = new ArrayList<>();

		File dir = new File(directory);
		File[] filesList = dir.listFiles();
		if (filesList != null) {
			for (File fName : filesList) {
				fileList.add(fName.getName());
			}
		}
		return fileList;
	}

	/**
	 * shutting down the executor service
	 * 
	 * @param executor
	 */
	public void shutdownExecutorService(ExecutorService executor) {
		try {
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			this.vlfLogger.write(GlobalConstants.LOG_CALLABLE_THREAD, VlfLogger.Category.SYSTEM_ERROR.name(),
					VlfLogger.Severity.ERROR, "FileReaderServiceManager::shutDownExecutorService(): " + e.getMessage(),
					VlfLogger.LogLevel.ERROR);
		}
	}

	/**
	 * close the open resources
	 * 
	 * @param randomAccessFile
	 * @param fileChannel
	 */
	public void closeResources(RandomAccessFile randomAccessFile, FileChannel fileChannel) {
		try {
			if (fileChannel != null)
				fileChannel.close();
			if (randomAccessFile != null)
				randomAccessFile.close();
		} catch (IOException e) {
			this.vlfLogger.write(GlobalConstants.LOG_OPERATION, Category.SYSTEM_ERROR.name(), Severity.ERROR,
					"FileReaderServiceManager::closeResources: " + e.getMessage(), LogLevel.ERROR);
			this.vlfLogger.write("FileReaderServiceManager::closeResources: " + e.getMessage(), Severity.ERROR, e);
		}
		this.vlfLogger.write(GlobalConstants.LOG_OPERATION, GlobalConstants.LOG_TYPE_SERVICE_LAYER, Severity.INFO,
				"FileReaderServiceManager::closeResources-Done", LogLevel.VERBOSE);
	}

	/**
	 * Copy content of a channel in a ByteBuffer Queue, reporting the original
	 * elements read from the file channel, buffered in a given size (bufferSize)
	 * 
	 * @param channel
	 *            Input Read Channel, considered open
	 * @param bufferSize
	 *            Size used to allocate read buffer
	 * @throws IOException
	 *             Any Input Output Exception that can occur reading the input file
	 *             channel
	 */
	public final Queue<ByteBuffer> readFromChannel(final FileChannel channel, int bufferSize) throws IOException {
		// Initializes the ByteBuffer
		final ByteBuffer buffer0 = ByteBuffer.allocate(bufferSize);
		// Reads byte from the (for assumption) open channel
		int bytes = channel.read(buffer0);
		// Creates the Output Queue
		Queue<ByteBuffer> bufferQueue = new ConcurrentLinkedQueue<>();
		// Checks previous read was successful
		if (bytes >= 0)
			bufferQueue.offer(buffer0);
		// Reads the rest of the file and place buffer data into the Output Queue
		// while file has bytes
		while (bytes >= 0) {
			// Creates/Allocates the Output ByteBuffer
			final ByteBuffer bufferN = ByteBuffer.allocate(bufferSize);
			// Reads next chunk of data
			bytes = channel.read(bufferN);
			// Checks read involved some bytes (no Empty Buffer are allowed -> waste of
			// memory)
			if (bytes >= 0)
				// Saves the full ByteBuffer into the queue without any manipulation (like flip,
				// trim, etc...)
				bufferQueue.offer(bufferN);
		}
		// Returns the Output Queue
		return bufferQueue;
	}

	public static final List<File> listAndFilterFilesRegExp(String folder, String regex, boolean recursive) {
		List<File> outList = new ArrayList<>(0);
		File fileDir = new File(folder);
		
		if ( fileDir.exists() && fileDir.isDirectory() ) {
			String[] files = fileDir.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.matches(regex);
				}
		    });
			outList.addAll(
			Arrays.asList(files).stream()
				.map( fn -> new File(fn) )
				.filter( f -> f.isFile() )
				.collect(Collectors.toList())
			);
			if ( recursive ) {
				outList.addAll(
				Arrays.asList(files).stream()
					.map( fn -> new File(fn) )
					.filter( f -> f.isDirectory() )
					.map( f -> listAndFilterFilesRegExp(f.getAbsolutePath(), regex, true) )
					.flatMap(List::stream)
					.collect(Collectors.toList())
				);
			}
		}
		return outList;
	}

	public static final List<File> listAndFilterFiles(String folder, String filter, boolean recursive) {
		List<File> outList = new ArrayList<>(0);
		File fileDir = new File(folder);
		
		if ( fileDir.exists() && fileDir.isDirectory() ) {
			File[] files = fileDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.contains(filter);
				}
		    });
			outList.addAll(
				Arrays.asList(files).stream()
					.filter( f -> f.isFile() )
					.collect(Collectors.toList())
			);
			if ( recursive ) {
				outList.addAll(
				Arrays.asList(files).stream()
					.filter( f -> f.isDirectory() )
					.map( f -> listAndFilterFiles(f.getAbsolutePath(), filter, true) )
					.flatMap(List::stream)
					.collect(Collectors.toList())
				);
			}
		}
		return outList;
	}

	public static final void createFolderIfNotExists(String pathName, VlfLogger vlfLogger) {
		try {
			if (pathName != null && !pathName.isEmpty()) {
				File d = new File(pathName);
				if ( ! d .exists() ) {
					d.mkdirs();
				} else if ( ! d.isDirectory() ) {
					d.setReadable(true);
					d.setWritable(true);
					d.delete();
					createFolderIfNotExists(pathName, vlfLogger);
					return;
				}
				d.setReadable(true);
				d.setWritable(true);
				if ( vlfLogger != null )
					LoggerHelper.logInfo(vlfLogger, "GenericHelper::createFolderIfNotExists", "Create folder: " + pathName);
			} else {
				if ( vlfLogger != null )
					LoggerHelper.logWarning(vlfLogger, "GenericHelper::createFolderIfNotExists", "Cannot create folder with empty name!!", null);
			}
		} catch (Exception e) {
			LoggerHelper.logError(vlfLogger, "GenericHelper::createFolderIfNotExists", null, VlfLogger.Category.SYSTEM_ERROR, e);
		}
		
	}

	/**
	 * Creates or overrides a file, writing required content
	 * @param filePath
	 * @param charset
	 * @param append
	 * @param lines
	 * @param vlfLogger
	 * @return
	 */
	public static boolean writeLinesToFile(String filePath, Charset charset, boolean append, boolean isBlockFile, List<String> lines, VlfLogger vlfLogger) {
		return writeLinesToFile(new File(filePath), charset, append, isBlockFile, lines, vlfLogger);
	}

	/**
	 * Creates or overrides a file, writing required content
	 * @param file
	 * @param charset
	 * @param append
	 * @param lines
	 * @param vlfLogger
	 * @return
	 */
	public static boolean writeLinesToFile(File file, Charset charset, boolean append, boolean isBlockFile, List<String> lines, VlfLogger vlfLogger) {
		try {
			if ( file == null ) {
				throw new NullPointerException("GenerichHelper::writeLinesToFile invalid file");
			}
			if ( ! file.exists() ) {
				file.createNewFile();
			}
			file.setReadable(true);
			file.setWritable(true);
			if ( isBlockFile ) {
				FileUtils.write(file, lines.stream()
						.reduce("", ( p, n ) -> p += n ), 
						charset, append);
			} else {
				FileUtils.write(file, lines.stream()
						.map( line -> line += "\n" )
						.reduce("", ( p, n ) -> p += n )
						.replace(".$"," "), 
						charset, append);
			}
			return true;
		} catch (Exception ex) {
			LoggerHelper.logError(vlfLogger, "GenerichHelper::writeLinesToFile", "Error etiring file: " + file.getAbsolutePath(), Category.SYSTEM_ERROR, ex);
			LoggerHelper.logError(vlfLogger, "GenerichHelper::writeLinesToFile", null, Category.SYSTEM_ERROR, ex);
		}
		return false;
	}

	/**
	 * @param filePath
	 * @param charset
	 * @param bufferSize
	 * @param vlfLogger
	 * @return
	 */
	public static List<String> loadLinesFromFile(String filePath, Charset charset, int bufferSize, VlfLogger vlfLogger) {
		return loadLinesFromFile( new File(filePath), charset, bufferSize, vlfLogger);
	}

	/**
	 * @param filePath
	 * @param charset
	 * @param bufferSize
	 * @param consumer
	 * @param vlfLogger
	 */
	public static void loadLinesFromFile(String filePath, Charset charset, int bufferSize, CompletionConsumer<String> consumer, VlfLogger vlfLogger) {
		loadLinesFromFile( new File(filePath), charset, bufferSize, consumer, vlfLogger);
	}
	/**
	 * @param file
	 * @param charset
	 * @param bufferSize
	 * @param vlfLogger
	 * @return
	 */
	public static List<String> loadLinesFromFile(File file, Charset charset, int bufferSize, VlfLogger vlfLogger) {
		List<String> lines = new ArrayList<String>();
		try  {
			if ( ! file.exists() ) {
				LoggerHelper.logError(vlfLogger, "GenerichHelper::loadLinesFromFile", "File: " + file.getAbsolutePath() + " doesn't exists!!", Category.SYSTEM_ERROR, null);
				return lines;
			}

			if ( ! file.canRead() )
				file.setReadable(true);
			
			if ( bufferSize == 0 ) {
				bufferSize = DEFAULT_BUFFER_SIZE;
			}
			
			try ( RandomAccessFile stream = new RandomAccessFile(file, "r");
				  //FileInputStream stream = new FileInputStream(file);
				  FileChannel channel = stream.getChannel() ) {
				MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
//				mappedByteBuffer.load();
				
		        byte[] data = new byte[bufferSize];
		        String rest = "";
		        while (mappedByteBuffer.hasRemaining()) {
		            int remaining = data.length;
	            	data = new byte[bufferSize];
		            if(mappedByteBuffer.remaining() < remaining) {
			              remaining = mappedByteBuffer.remaining();
			              data = new byte[remaining];
		            }
		            mappedByteBuffer.get(data, 0, remaining);
		            String buffStr = rest + new String(data, charset);
		            rest = "";
		            boolean endsWithTerm = buffStr.endsWith("\n");
		            List<String> list = new ArrayList<>();
		            String[] arr = buffStr.split("\n");
		            int arrLen = arr.length;
		            if ( endsWithTerm )
		            	list.addAll(Arrays.asList(arr));
		            else if ( arrLen > 1 ) {
		            	list.addAll(Arrays.asList(arr).subList(0, arrLen - 1));
		            	rest = arr[arrLen - 1];
		            }
			        lines.addAll(
			        		list
			        );
		            
		        }
		        if ( rest.length() > 0 ) {
		        	lines.add(rest);
		        }
			} catch ( Exception e) {
				LoggerHelper.logError(vlfLogger, "GenerichHelper::loadLinesFromFile", "Error reading file: " + file.getAbsolutePath(), Category.SYSTEM_ERROR, e);
			}
			
		} catch ( Exception ex) {
			LoggerHelper.logError(vlfLogger, "GenerichHelper::loadLinesFromFile", "Error reading file: " + file.getAbsolutePath(), Category.SYSTEM_ERROR, ex);
		}
		
		return lines;
	}

	/**
	 * @param file
	 * @param charset
	 * @param bufferSize
	 * @param consumer
	 * @param vlfLogger
	 */
	public static void loadLinesFromFile(File file, Charset charset, int bufferSize, CompletionConsumer<String> consumer, VlfLogger vlfLogger) {
		try  {
			if ( consumer == null ) {
				LoggerHelper.logError(vlfLogger, "GenerichHelper::loadLinesFromFile", "Error NO CONSUMER FOR file: " + file.getAbsolutePath(), Category.SYSTEM_ERROR, null);
				return ;
			}

			if ( ! file.exists() ) {
				LoggerHelper.logError(vlfLogger, "GenerichHelper::loadLinesFromFile", "File: " + file.getAbsolutePath() + " doesn't exists!!", Category.SYSTEM_ERROR, null);
				return ;
			}

			if ( ! file.canRead() )
				file.setReadable(true);
			
			if ( bufferSize == 0 ) {
				bufferSize = DEFAULT_BUFFER_SIZE;
			}
			
			try ( RandomAccessFile stream = new RandomAccessFile(file, "r");
				  //FileInputStream stream = new FileInputStream(file);
				  FileChannel channel = stream.getChannel() ) {
				MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
//				mappedByteBuffer.load();
				
		        byte[] data = new byte[bufferSize];
		        String rest = "";
		        while (mappedByteBuffer.hasRemaining()) {
		            int remaining = data.length;
	            	data = new byte[bufferSize];
		            if(mappedByteBuffer.remaining() < remaining) {
			              remaining = mappedByteBuffer.remaining();
			              data = new byte[remaining];
		            }
		            mappedByteBuffer.get(data, 0, remaining);
		            String buffStr = rest + new String(data, charset);
		            rest = "";
		            boolean endsWithTerm = buffStr.endsWith("\n");
		            List<String> list = new ArrayList<>();
		            String[] arr = buffStr.split("\n");
		            int arrLen = arr.length;
		            if ( endsWithTerm )
		            	list.addAll(Arrays.asList(arr));
		            else if ( arrLen > 1 ) {
		            	list.addAll(Arrays.asList(arr).subList(0, arrLen - 1));
		            	rest = arr[arrLen - 1];
		            }
			        consumer.acceptAll(
			        		list
			        );
		        }
		        if ( rest.length() > 0 ) {
		        	consumer.accept(rest);
		        }
			} catch ( Exception e) {
				LoggerHelper.logError(vlfLogger, "GenerichHelper::loadLinesFromFile", "Error reading file: " + file.getAbsolutePath(), Category.SYSTEM_ERROR, e);
			}
			
		} catch ( Exception ex) {
			LoggerHelper.logError(vlfLogger, "GenerichHelper::loadLinesFromFile", "Error reading file: " + file.getAbsolutePath(), Category.SYSTEM_ERROR, ex);
		}
		consumer.complete();
	}

	/**
	 * @param filePath
	 * @param charset
	 * @param recordLen
	 * @param bufferSize
	 * @param vlfLogger
	 * @return
	 */
	public static List<String> loadLinesFromFile(String filePath, Charset charset, int recordLen, int bufferSize, VlfLogger vlfLogger) {
		return loadLinesFromFile( new File(filePath), charset, recordLen, bufferSize, vlfLogger);
	}

	/**
	 * @param file
	 * @param charset
	 * @param recordLen
	 * @param recordsChunk
	 * @param vlfLogger
	 * @return
	 */
	public static List<String> loadLinesFromFile(File file, Charset charset, int recordLen, int recordsChunk, VlfLogger vlfLogger) {
		List<String> lines = new ArrayList<String>();
		if ( ! file.exists() ) {
			LoggerHelper.logError(vlfLogger, "GenerichHelper::loadLinesFromFile", "File: " + file.getAbsolutePath() + " doesn't exists!!", Category.SYSTEM_ERROR, null);
			return lines;
		}
	
		if ( ! file.canRead() )
			file.setReadable(true);
	
		try ( RandomAccessFile stream = new RandomAccessFile(file, "r");
			  //FileInputStream stream = new FileInputStream(file);
				FileChannel channel = stream.getChannel() ) {
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
	        buffer.load();
			lines.addAll(
				Splitter
	            .fixedLength(recordLen)
	            .splitToList(buffer.asCharBuffer().toString())
            );
		} catch ( Exception ex) {
			LoggerHelper.logError(vlfLogger, "GenerichHelper::loadLinesFromFile", "Error reading file: " + file.getAbsolutePath(), Category.SYSTEM_ERROR, ex);
		}
		
		return lines;
	}

	/**
	 * @param file
	 * @param charset
	 * @param recordLen
	 * @param recordsChunk
	 * @param consumer
	 * @param vlfLogger
	 */
	public static void loadLinesFromFile(File file, Charset charset, int recordLen, int recordsChunk, CompletionConsumer<String> consumer, VlfLogger vlfLogger) {
		
		if ( consumer == null ) {
			LoggerHelper.logError(vlfLogger, "GenerichHelper::loadLinesFromFile", "Error NO CONSUMER FOR file: " + file.getAbsolutePath(), Category.SYSTEM_ERROR, null);
			return ;
		}

		if ( ! file.exists() ) {
			LoggerHelper.logError(vlfLogger, "GenerichHelper::loadLinesFromFile", "File: " + file.getAbsolutePath() + " doesn't exists!!", Category.SYSTEM_ERROR, null);
			return ;
		}
	
		if ( ! file.canRead() )
			file.setReadable(true);
	
		try ( RandomAccessFile stream = new RandomAccessFile(file, "r");
				  //FileInputStream stream = new FileInputStream(file);
				FileChannel channel = stream.getChannel() ) {
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
	        buffer.load();
			consumer.acceptAll(
				Splitter
	            .fixedLength(recordLen)
	            .splitToList(buffer.asCharBuffer().toString())
			);
		} catch ( Exception ex) {
			LoggerHelper.logError(vlfLogger, "GenerichHelper::loadLinesFromFile", "Error reading file: " + file.getAbsolutePath(), Category.SYSTEM_ERROR, ex);
		}
		consumer.complete();
	}
	
	/**
	 * @param source
	 * @param destination
	 * @throws IOException
	 */
	public static final void moveFile(File source, File destination) throws IOException {
		if ( ! source.exists() ) {
			throw new IOException("MOVE_FILE: Source file: " + source.getAbsolutePath() + " does not exist!!");
		}
		if ( destination.exists() ) {
			destination.delete();
		}
		FileUtils.moveFile(source, destination);
	}

	/**
	 * @param source
	 * @param destination
	 * @throws IOException
	 */
	public static final void copyFile(File source, File destination) throws IOException {
		if ( ! source.exists() ) {
			throw new IOException("COPY_FILE: Source file: " + source.getAbsolutePath() + " does not exist!!");
		}
		if ( destination.exists() && destination.isFile() ) {
			destination.delete();
		} else if ( destination.exists() && ! destination.isFile() ) {
			throw new IOException("COPY_FILE: Destination: " + destination.getAbsolutePath() + " is not a file!!");
		}
		FileUtils.copyFile(source, destination);
	}
}