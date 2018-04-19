package com.mistdev.wooser.user;

import android.support.v4.app.FragmentManager;

import com.mistdev.mvc.view.ILoading;
import com.mistdev.mvc.view.IOptionsMenu;
import com.mistdev.wooser.data.models.User;
import com.mistdev.wooser.enums.BodyStructure;
import com.mistdev.wooser.enums.Gender;
import com.mistdev.wooser.enums.LifeStyle;

import java.util.ArrayList;

/**
 * Created by mcastro on 04/03/17.
 */

interface IUserView extends ILoading, IOptionsMenu {

    interface Listener {
        void onSaveUser(User user);
        void onHomeSelected();
        void genderClicked();
        void bodyStructureClicked();
        void lifeStyleClicked();
    }

    void setListener(Listener listener);

    void showDialogWithOptions(FragmentManager fragmentManager, ArrayList<String> options);
    void setUserGender(@Gender.Def int genderDef, String gender);
    void setUserBodyStructure(@BodyStructure.Def int bodyStructureDef, String bodyStructure);
    void setUserLifeStyle(@LifeStyle.Def int lifeStyleDef, String lifeStyle);
}
