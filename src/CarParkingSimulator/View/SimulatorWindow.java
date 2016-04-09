package CarParkingSimulator.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import CarParkingSimulator.Controller.*;
import CarParkingSimulator.Model.Garage;

public class SimulatorWindow extends JFrame
{
    //Code elements
    private Garage garage;
    private SimulatorEngine simulatorEngine;

    //GUI elements
    private JButton oneStepButton;
    private JButton hundredStepButton;

    private ParkingView parkingGarageView;
    private GraphView graphView;

    private JLabel stepLabel;

    public SimulatorWindow(int numberOfFloors, int numberOfRows, int numberOfPlaces)
    {
        garage = new Garage(numberOfFloors, numberOfRows, numberOfPlaces);
        simulatorEngine = new SimulatorEngine(garage);

        simulatorEngine.addListener(new SimulatorEngine.UpdateListener()
        {
            @Override
            public void DataUpdated(int currentStep)
            {
                updateView(currentStep);
            }
        });

        parkingGarageView = new ParkingView(garage);
        graphView = new GraphView(garage);

        stepLabel = new JLabel();

        oneStepButton = new JButton("One step");
        hundredStepButton = new JButton("Hundred step");

        oneStepButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                (new SimulationThread(1)).start();
            }
        });

        hundredStepButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                (new SimulationThread(100)).start();
            }
        });

        createLayout();

        updateView(0);
    }

    private void createLayout()
    {
        Container elementWindow = getContentPane();

        JPanel centreGridPanel = new JPanel();
        JPanel rightGridPanel = new JPanel();
        JPanel bottomGridPanel = new JPanel();
        JTabbedPane tabControl = new JTabbedPane();

        elementWindow.setLayout(new BorderLayout());
        rightGridPanel.setLayout(new BoxLayout(rightGridPanel, BoxLayout.Y_AXIS));
        centreGridPanel.setLayout(new GridLayout(1, 1));
        bottomGridPanel.setLayout(new GridLayout(1, 1));

        tabControl.add("ParkingView", parkingGarageView);
        tabControl.add("GraphView", graphView);

        centreGridPanel.add(tabControl);
        rightGridPanel.add(oneStepButton);
        rightGridPanel.add(hundredStepButton);
        bottomGridPanel.add(stepLabel);

        elementWindow.add(rightGridPanel, BorderLayout.WEST);
        elementWindow.add(centreGridPanel, BorderLayout.CENTER);
        elementWindow.add(bottomGridPanel, BorderLayout.SOUTH);

        pack();

        setVisible(true);
    }

    public void updateView(int currentStep)
    {
        stepLabel.setText(Integer.toString(currentStep));

        parkingGarageView.updateView();
        graphView.updateView();
    }

    public class SimulationThread extends Thread
    {
        private Thread thread;
        private int stepsToPerform;

        public SimulationThread(int stepsToPerform)
        {
            this.stepsToPerform = stepsToPerform;
        }

        public void run()
        {
            simulatorEngine.runSimulation(stepsToPerform);
        }

        public void start()
        {
            thread = new Thread(this, "SimulationThread");

            thread.start();
        }

    }
}
