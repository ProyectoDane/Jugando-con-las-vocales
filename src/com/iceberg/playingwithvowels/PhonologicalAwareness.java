/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels;

import java.util.HashMap;

import com.iceberg.playingwithvowels.helpers.VowelsHelper.VowelsEnum;

public class PhonologicalAwareness {
	public static int[] GetScreen(VowelsEnum vowel, int screen) {
		String key = String.valueOf(screen);
		switch (vowel) {
		case A:
			if (AScreens.containsKey(key)) {
				return AScreens.get(key);
			}
			break;
		case E:
			if (EScreens.containsKey(key)) {
				return EScreens.get(key);
			}
			break;
		case I:
			if (IScreens.containsKey(key)) {
				return IScreens.get(key);
			}
			break;
		case O:
			if (OScreens.containsKey(key)) {
				return OScreens.get(key);
			}
			break;
		case U:
			if (UScreens.containsKey(key)) {
				return UScreens.get(key);
			}
			break;
		default:
			return new int[5];
		}

		return new int[0];
	}

	public static int GetNextScreen(VowelsEnum vowel, int screen) {
		screen++;
		String key = String.valueOf(screen);
		switch (vowel) {
		case A:
			if (!AScreens.containsKey(key)) {
				screen = 1;
			}
			break;
		case E:
			if (!EScreens.containsKey(key)) {
				screen = 1;
			}
			break;
		case I:
			if (!IScreens.containsKey(key)) {
				screen = 1;
			}
			break;
		case O:
			if (!OScreens.containsKey(key)) {
				screen = 1;
			}
			break;
		case U:
			if (!UScreens.containsKey(key)) {
				screen = 1;
			}
			break;
		default:
			screen = 1;
			break;
		}

		return screen;
	}

	public static int GetScreensCount(VowelsEnum vowel) {

		switch (vowel) {
		case A:
			return AScreens.size();
		case E:
			return EScreens.size();
		case I:
			return IScreens.size();
		case O:
			return OScreens.size();
		case U:
			return UScreens.size();
		default:
			return 0;
		}
	}

	@SuppressWarnings("serial")
	private static final HashMap<String, int[]> AScreens = new HashMap<String, int[]>() {
		{
			put("1", new int[] { R.drawable.ic_luna, R.drawable.ic_agua, R.drawable.ic_silla });
			put("2", new int[] { R.drawable.ic_auto, R.drawable.ic_raton, R.drawable.ic_munieca });
			put("3", new int[] { R.drawable.ic_vestido, R.drawable.ic_abeja, R.drawable.ic_reloj });
			put("4", new int[] { R.drawable.ic_banana, R.drawable.ic_anana, R.drawable.ic_manzana });
			put("5", new int[] { R.drawable.ic_tren, R.drawable.ic_camion, R.drawable.ic_avion });
		}
	};

	@SuppressWarnings("serial")
	private static final HashMap<String, int[]> EScreens = new HashMap<String, int[]>() {
		{
			put("1", new int[] { R.drawable.ic_elefante, R.drawable.ic_oso, R.drawable.ic_zapato });
			put("2", new int[] { R.drawable.ic_perro, R.drawable.ic_remera, R.drawable.ic_espejo });
			put("3", new int[] { R.drawable.ic_escalera, R.drawable.ic_vestido, R.drawable.ic_reloj });
			put("4", new int[] { R.drawable.ic_moto, R.drawable.ic_leon, R.drawable.ic_escudo });
			put("5", new int[] { R.drawable.ic_ensalada, R.drawable.ic_campanas, R.drawable.ic_boton });
		}
	};

	@SuppressWarnings("serial")
	private static final HashMap<String, int[]> IScreens = new HashMap<String, int[]>() {
		{
			put("1", new int[] { R.drawable.ic_pollera, R.drawable.ic_bota_de_lluvia, R.drawable.ic_indio });
			put("2", new int[] { R.drawable.ic_tambor, R.drawable.ic_iguana, R.drawable.ic_heladera });
			put("3", new int[] { R.drawable.ic_isla, R.drawable.ic_guitarra, R.drawable.ic_zapatilla });
		}
	};

	@SuppressWarnings("serial")
	private static final HashMap<String, int[]> OScreens = new HashMap<String, int[]>() {
		{
			put("1", new int[] { R.drawable.ic_ojo, R.drawable.ic_lapicera, R.drawable.ic_galletitas });
			put("2", new int[] { R.drawable.ic_arbol, R.drawable.ic_paraguas, R.drawable.ic_ola });
			put("3", new int[] { R.drawable.ic_taza, R.drawable.ic_ojota, R.drawable.ic_caballo });
			put("4", new int[] { R.drawable.ic_cama, R.drawable.ic_oso, R.drawable.ic_pan });
		}
	};

	@SuppressWarnings("serial")
	private static final HashMap<String, int[]> UScreens = new HashMap<String, int[]>() {
		{
			put("1", new int[] { R.drawable.ic_lapiz, R.drawable.ic_gato, R.drawable.ic_unia });
			put("2", new int[] { R.drawable.ic_cuaderno, R.drawable.ic_pajaro, R.drawable.ic_uvas });
			put("3", new int[] { R.drawable.ic_tortuga, R.drawable.ic_uno, R.drawable.ic_mochila });
		}
	};
}
