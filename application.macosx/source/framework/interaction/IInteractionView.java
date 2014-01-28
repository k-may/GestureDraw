package framework.interaction;

import java.util.ArrayList;

import framework.data.UserData;
import framework.view.IView;



/**
 * @author kev
 *
 * Implementation should define logic for the indexing of users, how the pressure navigation works
 */
public interface IInteractionView extends IView {
	void removeUser(UserData user);

	ArrayList<UserData> get_users();

	UserData addUser(int id);

	UserData getUser(int id);
}
