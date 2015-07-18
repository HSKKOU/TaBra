package jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.ac.titech.itpro.sdl.tabra.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class BrainStormMainActivityFragment extends Fragment {

    public BrainStormMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_brain_storm_main, container, false);
    }
}
