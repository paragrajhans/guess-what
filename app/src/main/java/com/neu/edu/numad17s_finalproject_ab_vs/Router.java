package com.neu.edu.numad17s_finalproject_ab_vs;

import com.neu.edu.numad17s_finalproject_ab_vs.views.CreateAccountFragment;
import com.neu.edu.numad17s_finalproject_ab_vs.views.HomeFragment;
import com.neu.edu.numad17s_finalproject_ab_vs.views.HostGameFragment;
import com.neu.edu.numad17s_finalproject_ab_vs.views.JoinGameFragment;
import com.neu.edu.numad17s_finalproject_ab_vs.views.LivePlayerListFragment;
import com.neu.edu.numad17s_finalproject_ab_vs.views.OnlineGameFragment;
import com.neu.edu.numad17s_finalproject_ab_vs.views.ShowAndGuessFragment;
import com.neu.edu.numad17s_finalproject_ab_vs.views.SketchGuesserFragment;
import com.neu.edu.numad17s_finalproject_ab_vs.views.SketcherFragment;
import com.neu.edu.numad17s_finalproject_ab_vs.views.UserDetailsFragment;

/**
 * Created by ashishbulchandani on 08/04/17.
 */

public class Router {


    private static Router router;
    private  MainActivity mainActivityInstance;

    private Router (){}

    public static Router getInstance(){

        if(router==null)
            router = new Router();
        return router;

    }

    public void initialize(MainActivity mainActivity) {
        mainActivityInstance = mainActivity;
    }


    public void showHome(){
        mainActivityInstance.transact(new HomeFragment());
    }

    public void showGameSelectionFragment(){
        mainActivityInstance.transact(new OnlineGameFragment());
    }

    public void showUserDetailsFragment(){
        mainActivityInstance.transact(new UserDetailsFragment());
    }

    public void showHostFragment(){
        mainActivityInstance.transact(new HostGameFragment());
    }

    public void showJoinFragment(){
        mainActivityInstance.transact(new JoinGameFragment());
    }

    public void showSketcherFragment(){
        mainActivityInstance.transact(new SketcherFragment());
    }

    public void showSketchGuesserFragment(){
        mainActivityInstance.transact(new SketchGuesserFragment());
    }

    public void showCreateAccount() {
        mainActivityInstance.replace(new CreateAccountFragment());
    }

    public void showShowAndGuessFragment() {
        mainActivityInstance.transact(new ShowAndGuessFragment());
    }


    public void showLivePlayerListFragment(){
        mainActivityInstance.transact(new LivePlayerListFragment());
    }

}
