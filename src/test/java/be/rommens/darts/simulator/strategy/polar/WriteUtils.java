package be.rommens.darts.simulator.strategy.polar;

import be.rommens.darts.simulator.PolarCoordinatesService;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.chart.ui.Align;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class WriteUtils {

    public static void draw(XYSeries series) throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(series);

        ValueAxis radiusAxis = new NumberAxis();
        radiusAxis.setUpperBound(226);
        radiusAxis.setTickLabelsVisible(false);
        radiusAxis.setAxisLineVisible(false);
        radiusAxis.setTickMarksVisible(false);
        DefaultPolarItemRenderer renderer = new DefaultPolarItemRenderer();
        renderer.setShapesVisible(true);
        Stroke dashedStroke = new BasicStroke(
                0.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                0.0f, new float[] {0.0f, 1e10f}, 1.0f );
        renderer.setSeriesStroke(0, dashedStroke);

        ImageIcon icon = new ImageIcon(PolarCoordinatesService.class.getClassLoader().getResource("img.png"));

        PolarPlot plot = new PolarPlot(dataset, radiusAxis, renderer);
        plot.setCounterClockwise(false);
        plot.setBackgroundImage(icon.getImage());
        plot.setBackgroundImageAlignment(Align.FIT);
        //plot.setBackgroundImageAlpha(1.0f);
        plot.setMargin(0);
        plot.setInsets(RectangleInsets.ZERO_INSETS);
        plot.setBackgroundPaint(new Color(255,255,255,0));

        JFreeChart chart = new JFreeChart(null, plot);
        chart.setBackgroundPaint(new Color(255,255,255,0));
        chart.removeLegend();

        File file = new File("./out.png");
        ChartUtils.saveChartAsPNG(file, chart, 1000,1000);
    }

}
