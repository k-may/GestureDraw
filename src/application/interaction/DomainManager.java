package application.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PVector;

import application.view.MainView;

import framework.events.HandDetectedEvent;

public class DomainManager {
	protected HashMap<Integer, Integer> _handIdUserIdMap;
	protected Map<Integer, UserInputData> _domainData;

	private float firstDomain;
	private float secondDomain;

	public DomainManager(float fD, float sD) {
		firstDomain = fD;
		secondDomain = sD;

		_handIdUserIdMap = new HashMap<Integer, Integer>();
		_domainData = new HashMap<Integer, UserInputData>();
	}

	public UserInputData removeDomain(int id) {
		println("-->remove domain : " + id + " / " + _domainData.size());
		try {
			if (_domainData.containsKey(id)) {
				UserInputData data = _domainData.get(id);

				// remove domains / hand ids from map
				ArrayList<Integer> entries = new ArrayList<Integer>();
				for (Entry<Integer, Integer> set : _handIdUserIdMap.entrySet()) {
					if (set.getValue() == id)
						entries.add(set.getKey());
				}
				for (int handId : entries) {
					println("-->remove hand : " + handId);
					_handIdUserIdMap.remove(handId);
				}

				_domainData.remove(id);
				return data;
			} else {
				println("wierd : remove hand that doesn't exist!");
				return null;
			}
		} catch (Exception e) {
			println("cant remove hand: " + id);
			return null;
		}
	}

	public UserInputData getDomainData(int handId, PVector pos) {// int domain)
																	// {

		int domain = getDomainForHand(pos.x);

		UserInputData data = getHandForId(handId);

		// does hand exist?
		if (data == null) {
			// create only if domain not populated
			if (isDomainGood(domain)) {
				data = new UserInputData();
				data.domain = domain;
				_domainData.put(data.get_id(), data);
			}
		}

		// has domain changed?
		if (data != null) {
			if (data.domain != domain) {
				// only update if domain not populated
				if (!isDomainGood(domain))
					return null;
			}

			data.domain = domain;
		}
		
		/*
		 * if (data == null) { // DomainData data = null; int domain =
		 * getDomainForHand(pos.x); if (domain == -1) return null;
		 * 
		 * data = getHandForDomain(domain); }
		 */

		return data;
	}

	private Boolean isDomainGood(int domain) {
		// only two hands per domain
		int handCount = 0;
		for (Entry<Integer, Integer> set : _handIdUserIdMap.entrySet()) {
			if (set.getValue() == domain)
				handCount++;
		}
		return handCount <= 2;
	}

	private UserInputData getHandForId(int handId) {
		UserInputData data = null;

		if (_handIdUserIdMap.containsKey(handId)) {
			// check if hand has left domain
			int id = _handIdUserIdMap.get(handId);
			return _domainData.get(id);
		}
		return data;
	}


	private int getDomainForHand(float x) {
		return getDomainForNormalizedPos(x / MainView.SRC_WIDTH);
	}

	private int getDomainForNormalizedPos(float x) {

		int domain = -1;
		if (x <= firstDomain)
			domain = 0;
		else if (x > firstDomain & x <= secondDomain)
			domain = 1;
		else if (x > secondDomain && x <= 1)
			domain = 2;
		return domain;
	}

	public ArrayList<UserInputData> get_domainData() {
		ArrayList<UserInputData> readyData = new ArrayList<UserInputData>();

		for (UserInputData domain : _domainData.values()) {
			Boolean ready = domain.isReady();
			Boolean updated = domain.isUpdated();
			if (ready && updated)
				readyData.add(domain);
		}
		return readyData;
	}

	public void println(Object msg) {
		System.out.println(msg);
	}
}
