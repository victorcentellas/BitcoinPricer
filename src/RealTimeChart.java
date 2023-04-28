import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class RealTimeChart extends JPanel {
	private static SimpleConsumer consumer;
	private final TimeSeries series;
	private final Random random;
	private final JFreeChart chart;
	private final ChartPanel chartPanel;

	public RealTimeChart(final String title) {
		series = new TimeSeries("Price", Millisecond.class);
		random = new Random();

		final TimeSeriesCollection dataset = new TimeSeriesCollection(series);
		chart = ChartFactory.createTimeSeriesChart(title, "Time", "Value", dataset, true, true, false);

		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setRangeGridlinePaint(Color.BLACK);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesPaint(0, Color.RED);
		plot.setRenderer(renderer);

		final DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setAutoRange(true);

		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
		add(chartPanel, BorderLayout.CENTER);
	}

	public void update(float value) {
		final Millisecond now = new Millisecond();
		series.add(now, value);
	}

}