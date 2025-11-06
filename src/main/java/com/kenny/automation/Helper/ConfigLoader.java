package com.kenny.automation.Helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * <h1>ConfigLoader</h1> Utility class untuk membaca file konfigurasi
 * `config.properties` dan menyediakan method helper untuk mendapatkan value
 * berdasarkan key.
 *
 * <p>
 * <b>Fungsi utama:</b>
 * </p>
 * <ul>
 * <li>Memuat konfigurasi dari file
 * <code>src/main/java/MBI/DST/Resources/config.properties</code> saat class
 * pertama kali dipanggil.</li>
 * <li>Menyediakan method untuk mengambil value konfigurasi secara langsung
 * (<code>get()</code>).</li>
 * <li>Menyediakan method dengan fallback ke default value jika key tidak
 * ditemukan (<code>getOrDefault()</code>).</li>
 * <li>Mengecek apakah suatu key memiliki value yang valid
 * (<code>has()</code>).</li>
 * </ul>
 *
 * <p>
 * <b>Contoh Penggunaan:</b>
 * </p>
 * 
 * <pre>
 * Gunakan sebagian value capabilities dari config.properties
 * String deviceName = ConfigLoader.getOrDefault("deviceName", "iPhone 14 Pro");
 * if (ConfigLoader.has("udid")) {
 * 	String udid = ConfigLoader.get("udid");
 * 	System.out.println("Running on specific device: " + udid);
 * }
 * 
 * Gunakan value capabilites yang ada di config.properties
 * DesiredCapabilities caps = new DesiredCapabilities();
 * ConfigLoader.getAll().forEach(caps::setCapability);
 * </pre>
 *
 * <p>
 * <b>Catatan:</b> Jika file <code>config.properties</code> tidak ditemukan,
 * class ini akan menggunakan default value (jika disediakan oleh caller) dan
 * menampilkan peringatan di console.
 * </p>
 *
 * @author Kenny Ramadhan
 * @version 1.0
 */

public class ConfigLoader {

	/** Menyimpan semua konfigurasi dari file config.properties */
	private static Properties props = new Properties();

	// Static block untuk memuat file config.properties saat class dipanggil pertama
	// kali
	static {
		try {
			FileInputStream fis = new FileInputStream("src/main/java/MBI/DST/Resources/config.properties");
			props.load(fis);
			fis.close();
			System.out.println("Config loaded successfully.");
		} catch (IOException e) {
			System.err.println("Could not load config.properties. Using defaults where possible.");
		}
	}

	/**
	 * Mengambil value dari key yang ada di config.properties.
	 *
	 * @param key Nama key yang ingin diambil.
	 * @return Value dari key, atau <code>null</code> jika tidak ditemukan.
	 */
	public static String get(String key) {
		return props.getProperty(key);
	}

	/**
	 * Mengambil value dari key, jika tidak ditemukan maka kembalikan defaultValue.
	 *
	 * @param key          Nama key yang ingin diambil.
	 * @param defaultValue Value default jika key tidak ada.
	 * @return Value dari key atau defaultValue jika key tidak ditemukan atau
	 *         kosong.
	 */
	public static String getOrDefault(String key, String defaultValue) {
		String value = props.getProperty(key);
		return (value != null && !value.trim().isEmpty()) ? value : defaultValue;
	}

	/**
	 * Mengecek apakah key memiliki value yang valid.
	 *
	 * @param key Nama key yang ingin dicek.
	 * @return <code>true</code> jika key ada dan tidak kosong, <code>false</code>
	 *         jika tidak ada.
	 */
	public static boolean has(String key) {
		String value = props.getProperty(key);
		return value != null && !value.trim().isEmpty();
	}
	
	
	 /**
     * Mengambil semua konfigurasi sebagai Map<String, String>.
     * Berguna untuk set capability secara otomatis.
     */
    public static Map<String, String> getAll() {
        if (props.isEmpty()) return Collections.emptyMap();

        return props.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> String.valueOf(e.getKey()),
                        e -> String.valueOf(e.getValue())
                ));
    }

}

