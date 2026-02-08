package com.example.foodplaner.detailscreen.view;

public interface MealDetailsViewInterface {
    void onFavoriteStatusChanged(boolean isFavorite);
    void showMessage(String message);
    void onCalendarStatusChanged(boolean planned);

}
