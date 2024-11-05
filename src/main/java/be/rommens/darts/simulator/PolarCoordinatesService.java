package be.rommens.darts.simulator;

import static java.lang.Math.toDegrees;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.ImageIcon;
import org.apache.commons.geometry.euclidean.twod.PolarCoordinates;
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
import org.springframework.stereotype.Component;

@Component
public class PolarCoordinatesService {

    private static final int[] SEGMENT_VALUES = { 20, 1, 18, 4, 13, 6, 10, 15, 2, 17, 3, 19, 7, 16, 8, 11, 14, 9, 12, 5, 20 };

    public void calculate() throws IOException {
        PolarCoordinates single20 = PolarCoordinates.fromCartesian(0, 50);
        int index = Math.floorDiv(new BigDecimal(String.valueOf(single20.getAzimuth())).setScale(0, RoundingMode.HALF_UP).intValue() + 9, 18);
        int number = SEGMENT_VALUES[index];

        PolarCoordinates bull = PolarCoordinates.fromCartesian(0, 0);
        PolarCoordinates outerbull1 = PolarCoordinates.fromCartesian(0, 6.35);
        PolarCoordinates bullcenter = PolarCoordinates.fromCartesian(0, 11.25);
        PolarCoordinates outerbull2 = PolarCoordinates.fromCartesian(0, 15.9);
        PolarCoordinates innercenter = PolarCoordinates.fromCartesian(0, 57.45);
        PolarCoordinates inner = PolarCoordinates.fromCartesian(0, 99);
        PolarCoordinates treblecenter = PolarCoordinates.fromCartesian(0, 103);
        PolarCoordinates treble = PolarCoordinates.fromCartesian(0, 107);
        PolarCoordinates outercenter = PolarCoordinates.fromCartesian(0, 134.5);
        PolarCoordinates outer = PolarCoordinates.fromCartesian(0, 162);
        PolarCoordinates doublecenter = PolarCoordinates.fromCartesian(0, 166);
        PolarCoordinates doubled = PolarCoordinates.fromCartesian(0, 170);
        PolarCoordinates p = PolarCoordinates.fromCartesian(103,0);
        PolarCoordinates p2 = PolarCoordinates.fromCartesian(102, 16);
        PolarCoordinates p3 = PolarCoordinates.fromCartesian(102, -16);
        PolarCoordinates p4 = PolarCoordinates.fromCartesian(107, 0);
        PolarCoordinates p5 = PolarCoordinates.fromCartesian(99, 0);

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Boys");
        series1.add(toDegrees(p.getAzimuth()), p.getRadius());
        series1.add(toDegrees(p2.getAzimuth()), p2.getRadius());
        series1.add(toDegrees(p3.getAzimuth()), p3.getRadius());
        series1.add(toDegrees(p4.getAzimuth()), p4.getRadius());
        series1.add(toDegrees(p5.getAzimuth()), p5.getRadius());
        series1.add(toDegrees(bull.getAzimuth()), bull.getRadius());
        series1.add(toDegrees(bullcenter.getAzimuth()), bullcenter.getRadius());
        series1.add(toDegrees(inner.getAzimuth()), inner.getRadius());
        series1.add(toDegrees(innercenter.getAzimuth()), innercenter.getRadius());
        series1.add(toDegrees(treble.getAzimuth()), treble.getRadius());
        series1.add(toDegrees(treblecenter.getAzimuth()), treblecenter.getRadius());
        series1.add(toDegrees(outercenter.getAzimuth()), outercenter.getRadius());
        series1.add(toDegrees(outer.getAzimuth()), outer.getRadius());
        series1.add(toDegrees(doublecenter.getAzimuth()), doublecenter.getRadius());
        series1.add(toDegrees(doubled.getAzimuth()), doubled.getRadius());

        dataset.addSeries(series1);

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
