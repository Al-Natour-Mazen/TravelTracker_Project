package fr.univ_poitiers.dptinfo.traveltracker_project.Activities.SaveTripSteps;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import fr.univ_poitiers.dptinfo.traveltracker_project.DataBase.Entities.Trip;
import fr.univ_poitiers.dptinfo.traveltracker_project.DataBase.Entities.User;
import fr.univ_poitiers.dptinfo.traveltracker_project.DataBase.Repositories.UserRepository;
import fr.univ_poitiers.dptinfo.traveltracker_project.Fragments.BottomSaveTripStepsFragment;
import fr.univ_poitiers.dptinfo.traveltracker_project.R;
import fr.univ_poitiers.dptinfo.traveltracker_project.Session.SessionManager;
import fr.univ_poitiers.dptinfo.traveltracker_project.Utils.DataHelpers.LogHelper;
import fr.univ_poitiers.dptinfo.traveltracker_project.Utils.UIHelpers.Components.Calender.CalendarBinderComponent;
import fr.univ_poitiers.dptinfo.traveltracker_project.Utils.UIHelpers.Components.Calender.OnMyDateChangeListener;
import fr.univ_poitiers.dptinfo.traveltracker_project.Utils.UIHelpers.Helpers.ToastHelper;
import fr.univ_poitiers.dptinfo.traveltracker_project.Utils.UIHelpers.Helpers.VibrationManager;

import android.widget.EditText;
import android.widget.CalendarView;

public class SaveTripActivityStep1 extends AppCompatActivity implements OnMyDateChangeListener  {

    private static final String LOG_TAG = "SaveTripActivityStep1";
    private EditText editTextTitleTrip, editTextCity, editTextCountry;
    private CalendarView calendarViewStartTravel;
    private int userId;
    private BottomSaveTripStepsFragment fragment;
    private String selectedDate;
    private Trip newTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);

        // Set layout
        setContentView(R.layout.activity_save_trip_step1);

        // Apply system window insets for edge-to-edge display
        applySystemWindowsInsets();

        // Initialize UI components
        initComponents();

        // Initialize session and setup buttons
        initializeSession();
        setupListeners();

        // Setup text watchers for input validation
        setupTextWatchers();

        newTrip = new Trip();

        // Setup the fragment
        fragment = BottomSaveTripStepsFragment.newInstance(SaveTripActivityStep2.class);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerBottom, fragment)
                .commit();
    }

    // Initialize UI components
    private void initComponents() {
        editTextTitleTrip = findViewById(R.id.editTextTitleTrip);
        editTextCity = findViewById(R.id.editTextCity);
        editTextCountry = findViewById(R.id.editTextCountry);
        calendarViewStartTravel = findViewById(R.id.calendarViewStartTravel);
    }

    // Apply system window insets to adjust layout with edge-to-edge display
    private void applySystemWindowsInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Setup text watchers to enable/disable the next button based on input validation
    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableOrDisableNextButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        editTextTitleTrip.addTextChangedListener(textWatcher);
        editTextCity.addTextChangedListener(textWatcher);
        editTextCountry.addTextChangedListener(textWatcher);
    }

    // Setup listeners
    private void setupListeners() {
        CalendarBinderComponent calanderbinder = new CalendarBinderComponent(calendarViewStartTravel, this);
    }

    // Enable or disable the next button based on input validation
    private void enableOrDisableNextButton() {
        String title = editTextTitleTrip.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String country = editTextCountry.getText().toString().trim();

        boolean enableButton = !title.isEmpty() && !city.isEmpty() && !country.isEmpty();

        if (enableButton) {
            prepareTrip();
            fragment.setEnableNextBtn(true);
        }else {
            fragment.setEnableNextBtn(false);
        }
    }

    // Initialize session to get user ID
    private void initializeSession() {
        UserRepository userRepository = new UserRepository(SaveTripActivityStep1.this.getApplication());
        SessionManager session = SessionManager.getInstance(SaveTripActivityStep1.this, userRepository);
        LiveData<User> userLiveData = session.getLoggedInUser();
        userLiveData.observe(this, user -> {
            if (user != null) {
                userId = user.getId();
            }
        });
    }

    // Go to the next step of trip saving process
    private void prepareTrip() {
        newTrip.setUserId(userId);
        newTrip.setCity(editTextCity.getText().toString());
        newTrip.setCountry(editTextCountry.getText().toString());
        newTrip.setName(editTextTitleTrip.getText().toString());
        if(selectedDate != null)
            newTrip.setDepartureDate(selectedDate);
        fragment.setTrip(newTrip);
    }

    @Override
    public void onDateChange(String date) {
        selectedDate = date;
        prepareTrip();
    }
}
