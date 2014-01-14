package framework;

public class RegionHelper {

	public static float HorUserRegion1;
	public static float HorUserRegion2;

	public static int GetUserRegion(float ratio) {
		if (ratio <= HorUserRegion1)
			return 0;
		else if (ratio > HorUserRegion1 && ratio <= HorUserRegion2)
			return 1;
		else
			return 2;
	}
}
