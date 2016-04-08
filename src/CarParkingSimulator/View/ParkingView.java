package CarParkingSimulator.View;

import CarParkingSimulator.Controller.GarageHelper;
import CarParkingSimulator.Model.*;

import javax.swing.*;
import java.awt.*;

public class ParkingView extends JPanel
{
    private GarageHelper garageHelper = null;

    private Dimension size;
    private Image carParkImage;

    /**
     * Constructor for objects of class CarPark
     */
    public ParkingView(GarageHelper helper)
    {
        garageHelper = helper;

        size = new Dimension(0, 0);
    }

    /**
     * Overridden. Tell the GUI manager how big we would like to be.
     */
    public Dimension getPreferredSize()
    {
        return new Dimension(800, 500);
    }

    /**
     * Overriden. The car park view component needs to be redisplayed. Copy the
     * internal image to screen.
     */
    public void paintComponent(Graphics g)
    {
        if (carParkImage == null)
        {
            return;
        }

        Dimension currentSize = getSize();

        if (size.equals(currentSize))
        {
            g.drawImage(carParkImage, 0, 0, null);
        }
        else
        {
            // Rescale the previous image.
            g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
        }
    }

    public void updateView()
    {
        // Create a new car park image if the size has changed.
        if (!size.equals(getSize()))
        {
            size = getSize();

            carParkImage = createImage(size.width, size.height);
        }

        Graphics graphics = carParkImage.getGraphics();

        for(int floor = 0; floor < garageHelper.getNumberOfFloors(); floor++)
        {
            for(int row = 0; row < garageHelper.getNumberOfRows(); row++)
            {
                for(int place = 0; place < garageHelper.getNumberOfPlaces(); place++)
                {
                    Location location = new Location(floor, row, place);

                    Car car = garageHelper.getCarAt(location);

                    //if a spot is reserved change the color to blue instead of red
                    Color color1 = car == null ? Color.white : Color.red;
                    Color color2 = Color.blue;

                    boolean test = location.getReservation();

                    if(!location.getReservation())
                        drawPlace(graphics, location, color1);
                    else
                        drawPlace(graphics, location, color2);
                }
            }
        }

        repaint();
    }

    /**
     * Paint a place on this car park view in a given color.
     */
    private void drawPlace(Graphics graphics, Location location, Color color)
    {
        graphics.setColor(color);

        graphics.fillRect(location.getFloor() * 260 + (1 + (int)Math.floor(location.getRow() * 0.5)) * 75 + (location.getRow() % 2) * 20, 60 + location.getPlace() * 10, 20 - 1, 10 - 1); // TODO use dynamic size or constants
    }
}