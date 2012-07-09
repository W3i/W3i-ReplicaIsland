package com.w3i.replica.replicaisland.store;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.w3i.common.Log;
import com.w3i.replica.replicaisland.achivements.Achievement;
import com.w3i.replica.replicaisland.achivements.AchievementManager;
import com.w3i.replica.replicaisland.achivements.CrystalsAchievement;
import com.w3i.replica.replicaisland.achivements.FlyTime;
import com.w3i.replica.replicaisland.achivements.GoodEndingAchievement;
import com.w3i.replica.replicaisland.achivements.JetpackTime;
import com.w3i.replica.replicaisland.achivements.PearlsAchievement;
import com.w3i.replica.replicaisland.achivements.ProgressAchievement;

public class SharedPreferenceManager {
	private SharedPreferences preferences;
	private static SharedPreferenceManager instance;

	private static final String PREF_FILE_NAME = "ReplicaIslandStorePreferences";

	// Funds Manager preferences
	private static final String PREF_FUNDS_PEARLS = "totalPearls";
	private static final String PREF_FUNDS_CRYSTALS = "totalCrystals";

	// Store preferences
	private static final String PREF_PURCHASED_ITEMS = "purchasedItems";

	// Items preferences
	private static final String PREF_POWERUP_LIFE_UPGRADE = "lifeUpgrade";
	private static final String PREF_POWERUP_JETPACK_DURATION_UPGRADE = "jetpackDurationUpgrade";
	private static final String PREF_POWERUP_JETPACK_GROUND_REFILL_UPGRADE = "jetpackGroundRefillUpgrade";
	private static final String PREF_POWERUP_JETPACK_AIR_REFILL_UPGRADE = "jetpackAirRefillUpgrade";
	private static final String PREF_POWERUP_SHIELD_DURATION_UPGRADE = "shieldDurationUpgrade";
	private static final String PREF_POWERUP_SHIELD_PEARLS_UPGRADE = "siheldPearlsUpgrade";
	private static final String PREF_POWERUP_MONSTER_KILL_VALUE_UPGRADE = "monsterKillValueUpgrade";
	private static final String PREF_POWERUP_KILLING_SPREE_ENABLED = "killingSpreeEnabled";
	private static final String PREF_POWERUP_KILLING_SPREE_VALUE = "killingSpreeValue";
	private static final String PREF_POWERUP_GARBAGE_COLLECTOR_ENABLED = "garbageCollectorEnabled";
	private static final String PREF_POWERUP_GARBAGE_COLLECTOR_PEARLS = "garbageCollectorPearls";
	private static final String PREF_CRYSTAL_EXTRACTOR_CRYSTALS = "crystalExtractorCrystals";
	private static final String PREF_CRYSTAL_EXTRACTRO_MONSTERS = "crystalExctactorMonsters";

	// Achievements preferences
	private static final String PREF_ACHIEVEMENT_CRYSTALS = "achvCrystals";
	private static final String PREF_ACHIEVEMENT_CRYSTALS_DONE = "achvCrystalsDone";
	private static final String PREF_ACHIEVEMENT_CRYSTALS_PROGRESS = "achvCrystalsProgress";
	private static final String PREF_ACHIEVEMENT_PEARLS = "achvPearls";
	private static final String PREF_ACHIEVEMENT_PEARLS_DONE = "achvPearlsDone";
	private static final String PREF_ACHIEVEMENT_PEARLS_PROGRESS = "achvPearlsProgress";
	private static final String PREF_ACHIEVEMENT_GOOD_ENDINNG = "achvGoodEnding";
	private static final String PREF_ACHIEVEMENT_GOOD_ENDINNG_DONE = "achvGoodEndingDone";
	private static final String PREF_ACHIEVEMENT_AIR_TIME = "achvAirTime";
	private static final String PREF_ACHIEVEMENT_AIR_TIME_DONE = "achvAirTimeDone";
	private static final String PREF_ACHIEVEMENT_AIR_TIME_PROGRESS = "achvAirTimeProgress";
	private static final String PREF_ACHIEVEMENT_JETPACK_TIME = "achvJetpackTime";
	private static final String PREF_ACHIEVEMENT_JETPACK_TIME_DONE = "achvJetpackTimeDone";
	private static final String PREF_ACHIEVEMENT_JETPACK_TIME_PROGRESS = "achvJetpackTimeProgress";

	private SharedPreferenceManager(Context context) {
		instance = this;
		preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
	}

	public static void initialize(
			Context context) {
		new SharedPreferenceManager(context);
	}

	private static void checkInstance() {
		if (instance == null) {
			throw new IllegalStateException("SharedPreferencesManager not initialized.");
		}
	}

	static void loadFunds() {
		checkInstance();
		instance._loadFunds();
	}

	private void _loadFunds() {
		try {
			int pearls = preferences.getInt(PREF_FUNDS_PEARLS, 0);
			int crystals = preferences.getInt(PREF_FUNDS_CRYSTALS, 0);
			FundsManager.setCrystals(crystals);
			FundsManager.setPearls(pearls);
		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while loading funds");
		}
	}

	static void storeFunds() {
		checkInstance();
		instance._storeFunds();
	}

	private void _storeFunds() {
		try {
			Editor edit = preferences.edit();
			edit.putInt(PREF_FUNDS_PEARLS, FundsManager.getPearls());
			edit.putInt(PREF_FUNDS_CRYSTALS, FundsManager.getCrystals());
			edit.commit();
		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while storing funds");
		}

	}

	static void loadPurchasedItems() {
		checkInstance();
		instance._loadPurchasedItems();

	}

	private void _loadPurchasedItems() {
		try {
			String jsonItems = preferences.getString(PREF_PURCHASED_ITEMS, null);
			List<Long> purchasedItemsIds = null;
			Log.i("SharedPreferenceManager: Loaded item ids: " + jsonItems);
			if (jsonItems != null) {
				Type arrayType = new TypeToken<List<Long>>() {
				}.getType();
				purchasedItemsIds = new Gson().fromJson(jsonItems, arrayType);
			}
			ItemManager.loadPurchasedItems(purchasedItemsIds);
		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while loading purchased items", e);
		}

	}

	static void storePurchasedItems() {
		checkInstance();
		instance._storePurchasedItems();
	}

	private void _storePurchasedItems() {
		try {
			List<Long> purchasedItemsIds = ItemManager.getPurchasedItemsIds();
			if (purchasedItemsIds == null) {
				return;
			}
			Editor edit = preferences.edit();
			String json = new Gson().toJson(purchasedItemsIds);
			edit.putString(PREF_PURCHASED_ITEMS, json);
			edit.commit();
		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while storing purchased items");
		}
	}

	static void loadPowerups() {
		checkInstance();
		instance._loadPowerups();
	}

	private void _loadPowerups() {
		try {
			int lifeUpgrade = preferences.getInt(PREF_POWERUP_LIFE_UPGRADE, 0);
			int monsterValue = preferences.getInt(PREF_POWERUP_MONSTER_KILL_VALUE_UPGRADE, 0);

			float jetpackDuration = preferences.getFloat(PREF_POWERUP_JETPACK_DURATION_UPGRADE, 0.0f);
			float jetpackGroundRefill = preferences.getFloat(PREF_POWERUP_JETPACK_GROUND_REFILL_UPGRADE, 0.0f);
			float jetpackAirRefill = preferences.getFloat(PREF_POWERUP_JETPACK_AIR_REFILL_UPGRADE, 0.0f);

			float shieldDuration = preferences.getFloat(PREF_POWERUP_SHIELD_DURATION_UPGRADE, 0.0f);
			int shiledPearls = preferences.getInt(PREF_POWERUP_SHIELD_PEARLS_UPGRADE, 0);

			PowerupManager.setLifeUpgrade(lifeUpgrade);
			PowerupManager.setMonsterValue(monsterValue);
			PowerupManager.setJetpackAirRefill(jetpackAirRefill);
			PowerupManager.setJetpackDuration(jetpackDuration);
			PowerupManager.setJetpackGroundRefill(jetpackGroundRefill);
			PowerupManager.setShieldDuration(shieldDuration);
			PowerupManager.setShiledPearls(shiledPearls);

			boolean isGarbageCollectorEnabled = preferences.getBoolean(PREF_POWERUP_GARBAGE_COLLECTOR_ENABLED, false);
			PowerupManager.setGarbageCollector(isGarbageCollectorEnabled);

			if (isGarbageCollectorEnabled) {
				int garbageCollectorPearls = preferences.getInt(PREF_POWERUP_GARBAGE_COLLECTOR_PEARLS, 0);
				boolean isKillinSpreeEnabled = preferences.getBoolean(PREF_POWERUP_KILLING_SPREE_ENABLED, false);
				int crystalsPerKill = preferences.getInt(PREF_CRYSTAL_EXTRACTOR_CRYSTALS, 0);
				int monstersForCrystal = preferences.getInt(PREF_CRYSTAL_EXTRACTRO_MONSTERS, 0);
				PowerupManager.setMonsterValue(garbageCollectorPearls);
				PowerupManager.setCrystalsPerKill(crystalsPerKill);
				PowerupManager.setKillsForCrystal(monstersForCrystal);
				PowerupManager.setKillingSpreeEnabled(isKillinSpreeEnabled);

				if (isKillinSpreeEnabled) {
					float killingSpreeMultiplier = preferences.getFloat(PREF_POWERUP_KILLING_SPREE_VALUE, 0);
					PowerupManager.setKillingSpreeBonus(killingSpreeMultiplier);
				}
			}

		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while loading powerups");
		}
	}

	static void storePowerups() {
		checkInstance();
		instance._storePowerups();
	}

	private void _storePowerups() {
		try {
			Editor edit = preferences.edit();
			edit.putInt(PREF_POWERUP_LIFE_UPGRADE, PowerupManager.getLifeUpgrade());
			edit.putInt(PREF_POWERUP_MONSTER_KILL_VALUE_UPGRADE, PowerupManager.getMonsterValue());

			edit.putFloat(PREF_POWERUP_JETPACK_DURATION_UPGRADE, PowerupManager.getJetpackDuration());
			edit.putFloat(PREF_POWERUP_JETPACK_GROUND_REFILL_UPGRADE, PowerupManager.getJetpackGroundRefill());
			edit.putFloat(PREF_POWERUP_JETPACK_AIR_REFILL_UPGRADE, PowerupManager.getJetpackAirRefill());

			edit.putFloat(PREF_POWERUP_SHIELD_DURATION_UPGRADE, PowerupManager.getShieldDuration());
			edit.putInt(PREF_POWERUP_SHIELD_PEARLS_UPGRADE, PowerupManager.getShiledPearls());
			edit.putBoolean(PREF_POWERUP_GARBAGE_COLLECTOR_ENABLED, PowerupManager.hasGarbageCollector());

			if (PowerupManager.hasGarbageCollector()) {
				edit.putInt(PREF_POWERUP_GARBAGE_COLLECTOR_PEARLS, PowerupManager.getPearlsPerKill());
				edit.putInt(PREF_CRYSTAL_EXTRACTOR_CRYSTALS, PowerupManager.getCrystalsPerKill());
				edit.putInt(PREF_CRYSTAL_EXTRACTRO_MONSTERS, PowerupManager.getKillsForCrystal());
				edit.putBoolean(PREF_POWERUP_KILLING_SPREE_ENABLED, PowerupManager.isKillingSpreeEnabled());

				if (PowerupManager.isKillingSpreeEnabled()) {
					edit.putFloat(PREF_POWERUP_KILLING_SPREE_VALUE, PowerupManager.getKillingSpreeBonus());
				}
			}
			edit.commit();
		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while storing powerups");
		}

	}

	public static void storeAchivements() {
		checkInstance();
		instance._storeAchivements();
	}

	private void _storeAchivements() {
		try {
			Editor edit = preferences.edit();
			for (Achievement achv : AchievementManager.getAchivements()) {
				_storeAchievement(achv, edit);
			}
			edit.commit();
		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while storing achivements");
		}
	}

	private void _storeAchievement(
			Achievement achv,
			Editor edit) {
		switch (achv.getType()) {
		case CRYSTALS:
			edit.putBoolean(PREF_ACHIEVEMENT_CRYSTALS, achv.isDisabled());
			edit.putBoolean(PREF_ACHIEVEMENT_CRYSTALS_DONE, achv.isDone());
			edit.putInt(PREF_ACHIEVEMENT_CRYSTALS_PROGRESS, ((ProgressAchievement) achv).getProgress());
			break;
		case GOOD_ENDING:
			edit.putBoolean(PREF_ACHIEVEMENT_GOOD_ENDINNG, achv.isDisabled());
			edit.putBoolean(PREF_ACHIEVEMENT_GOOD_ENDINNG_DONE, achv.isDone());
			break;
		case PEARLS:
			edit.putBoolean(PREF_ACHIEVEMENT_PEARLS, achv.isDisabled());
			edit.putBoolean(PREF_ACHIEVEMENT_PEARLS_DONE, achv.isDone());
			edit.putInt(PREF_ACHIEVEMENT_PEARLS_PROGRESS, ((ProgressAchievement) achv).getProgress());
			break;
		case FLY_TIME:
			edit.putBoolean(PREF_ACHIEVEMENT_AIR_TIME, achv.isDisabled());
			edit.putBoolean(PREF_ACHIEVEMENT_AIR_TIME_DONE, achv.isDone());
			edit.putInt(PREF_ACHIEVEMENT_AIR_TIME_PROGRESS, ((ProgressAchievement) achv).getProgress());
			break;
		case JETPACK_TIME:
			edit.putBoolean(PREF_ACHIEVEMENT_JETPACK_TIME, achv.isDisabled());
			edit.putBoolean(PREF_ACHIEVEMENT_JETPACK_TIME_DONE, achv.isDone());
			edit.putInt(PREF_ACHIEVEMENT_JETPACK_TIME_PROGRESS, ((ProgressAchievement) achv).getProgress());
			break;
		}
	}

	public static void loadAchivements() {
		checkInstance();
		instance._loadAchivements();
	}

	private void _loadAchivements() {
		try {
			boolean crystalsDisabled = preferences.getBoolean(PREF_ACHIEVEMENT_CRYSTALS, false);
			boolean crystalsDone = preferences.getBoolean(PREF_ACHIEVEMENT_CRYSTALS_DONE, false);
			int crystalsProgress = preferences.getInt(PREF_ACHIEVEMENT_CRYSTALS_PROGRESS, 0);
			if (!crystalsDisabled) {
				CrystalsAchievement crystalsAchievement = new CrystalsAchievement();
				crystalsAchievement.setProgress(crystalsProgress);
				crystalsAchievement.setDone(crystalsDone);
				AchievementManager.addAchivement(crystalsAchievement);
			}

			boolean goodEndingDisabled = preferences.getBoolean(PREF_ACHIEVEMENT_GOOD_ENDINNG, false);
			boolean goodEndingDone = preferences.getBoolean(PREF_ACHIEVEMENT_GOOD_ENDINNG_DONE, false);
			if (!goodEndingDisabled) {
				GoodEndingAchievement goodEndingAchievement = new GoodEndingAchievement();
				goodEndingAchievement.setDone(goodEndingDone);
				AchievementManager.addAchivement(goodEndingAchievement);
			}

			boolean pearlsDisabled = preferences.getBoolean(PREF_ACHIEVEMENT_PEARLS, false);
			boolean pearlsDone = preferences.getBoolean(PREF_ACHIEVEMENT_PEARLS_DONE, false);
			int pearlsProgress = preferences.getInt(PREF_ACHIEVEMENT_PEARLS_PROGRESS, 0);
			if (!pearlsDisabled) {
				PearlsAchievement pearlsAchievement = new PearlsAchievement();
				pearlsAchievement.setDone(pearlsDone);
				pearlsAchievement.setProgress(pearlsProgress);
				AchievementManager.addAchivement(pearlsAchievement);
			}

			boolean airTimeDisabled = preferences.getBoolean(PREF_ACHIEVEMENT_AIR_TIME, false);
			boolean airTimeDone = preferences.getBoolean(PREF_ACHIEVEMENT_AIR_TIME_DONE, false);
			int airTimeProgress = preferences.getInt(PREF_ACHIEVEMENT_AIR_TIME_PROGRESS, 0);
			if (!airTimeDisabled) {
				FlyTime airTimeAchievement = new FlyTime();
				airTimeAchievement.setDone(airTimeDone);
				airTimeAchievement.setProgress(airTimeProgress);
				AchievementManager.addAchivement(airTimeAchievement);
			}

			boolean jetpackDisabled = preferences.getBoolean(PREF_ACHIEVEMENT_JETPACK_TIME, false);
			boolean jetpackDone = preferences.getBoolean(PREF_ACHIEVEMENT_JETPACK_TIME_DONE, false);
			int jetpackProgress = preferences.getInt(PREF_ACHIEVEMENT_JETPACK_TIME_PROGRESS, 0);
			if (!jetpackDisabled) {
				JetpackTime jetPackTimeAchievement = new JetpackTime();
				jetPackTimeAchievement.setDone(jetpackDone);
				jetPackTimeAchievement.setProgress(jetpackProgress);
				AchievementManager.addAchivement(jetPackTimeAchievement);
			}

		} catch (Exception e) {
			Log.e("SharedPreferenceManager: Unexpected exception caught while loading achivements");
		}
	}

	public static void release() {
		if (instance == null) {
			return;
		}
		instance.preferences = null;
		instance = null;
	}
}
