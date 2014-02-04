package application.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PVector;

import application.view.MainView;

import framework.events.HandDetectedEvent;
import framework.events.UpdateColorEvent;

public class DomainManager {
	
	private static DomainManager _instance;
	public static DomainManager getInstance(){
		if(_instance == null)
			_instance = new DomainManager();
		
		return _instance;
	}
	
	protected HashMap<Integer, Integer> _handIdUserIdMap;
	protected Map<Integer, UserInputData> _domainData;
	private ArrayList<Integer> _falseDomains;

	private float firstDomain;
	private float secondDomain;

	private DomainManager() {
		firstDomain = MainView.DOMAIN_1;
		secondDomain = MainView.DOMAIN_2;

		_handIdUserIdMap = new HashMap<Integer, Integer>();
		_domainData = new HashMap<Integer, UserInputData>();
		_falseDomains = new ArrayList<Integer>();
	}

	public UserInputData removeDomain(int id) {
		println("\n===================== REMOVE ===================");
		println("size : " + _domainData.size() + ", ready : "
				+ get_domainData().size());
		try {
			if (_domainData.containsKey(id)) {
				UserInputData data = _domainData.get(id);

				// remove domains / hand ids from map
				ArrayList<Integer> entries = new ArrayList<Integer>();
				for (Entry<Integer, Integer> set : _handIdUserIdMap.entrySet()) {
					if (set.getValue() == id)
						entries.add(set.getKey());
				}
				int handId = -1;
				for (int i : entries) {
					println("-->remove hand : " + i);
					handId = i;
					_handIdUserIdMap.remove(handId);
				}
				println(" id = " + id + ", handId = " + handId);
				println(" --> size : " + _domainData.size() + ", ready : "
						+ get_domainData().size());

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

	public UserInputData getDomainData(int handId, float xPos) {// int domain)
																// {
		int domain = getDomainForHand(xPos);

		UserInputData data = getHandForId(handId);

		// does hand exist?
		if (data == null) {
			// create only if domain not populated
			if (isDomainGood(domain))
				data = createDomain(handId, domain);
		}

		// has domain changed?
		if (data != null) {
			if (data.domain != domain) {
				// only update if domain not populated
				if (!isDomainGood(domain))
					return null;
			}

			data.domain = domain;
			data.updated = true;
		}

		return data;
	}

	private UserInputData createDomain(int handId, int domain) {
		println("\n=================ADD=====================");
		println("hand =" + handId + ", domain = " + domain);

		UserInputData data = new UserInputData();
		data.domain = domain;
		_handIdUserIdMap.put(handId, data.get_id());
		_domainData.put(data.get_id(), data);

		println("-->size : " + _domainData.size() + ", ready : "
				+ get_domainData().size());


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


		for (Entry<Integer, UserInputData> entry : _domainData.entrySet()) {
			UserInputData domain = entry.getValue();
			Boolean ready = domain.isReady();
			Boolean good = domain.isGood();
			Boolean updated = domain.updated;
			if (ready){
				if(good)
					readyData.add(domain);
			} else if(!updated)
				_falseDomains.add(entry.getKey());
			
			domain.updated = false;
		}

		/*
		 * if data isn't ready nor updated, we need to clear it before
		 * initializing the interactive view (or it'll never get removed!)
		 */
		for (Integer i : _falseDomains) {
			println("remove false : " + i);
			_domainData.remove(i);
		}
		_falseDomains = new ArrayList<Integer>();

		
		return readyData;
	}

	public int getDataCount() {
		return _domainData.values().size();
	}

	public void println(Object msg) {
		//System.out.println(msg);
	}
}
