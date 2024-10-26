package be.rommens.darts.simulator;

import static java.lang.Math.toDegrees;

import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Statistics;
import be.rommens.darts.simulator.model.Turn;
import be.rommens.darts.simulator.strategy.polar.TrebleThrowSimulationStrategy;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import org.apache.commons.geometry.euclidean.twod.PolarCoordinates;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("polar")
@SpringBootTest
public class legSimulatorPolarTest {

    @Autowired
    private legSimulator legSimulator;

    @Test
    void singleGameTest() {
        var leg = legSimulator.playGame(new Player("Humphries",25,50,95,42,44, 43));
        for(Turn turn : leg) {
            System.out.println(turn.getStartScore() + " : " + turn.getScoreThrown() + " (" + turn + ")");
        }
        Statistics.calculate(leg).write();
    }

    @Test
    void testBatch() {
        List<Statistics> statistics = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            var result = legSimulator.playGame(new Player("Humphries",25,50,95,42,44, 43));
            statistics.add(Statistics.calculate(result));
        }
    }


    // Test for drawing circle
    @Test
    void testTrebleSpread() throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Boys");
        for (int i=0; i < 100000; i++) {
            var aim = Vector2D.of(103,0);
            //var aim = Vector2D.of(-61,-83);
            var point = Vector2D.of(
                    ThreadLocalRandom.current().nextGaussian(aim.getX(), 20),
                    ThreadLocalRandom.current().nextGaussian(aim.getY(), 20));


            PolarCoordinates result2 = PolarCoordinates.fromCartesian(point);

            PolarCoordinates center = PolarCoordinates.fromCartesian(aim);
            var center2d = center.toCartesian();
            var punt2d = result2.toCartesian();
            var rek = ((punt2d.getX() - center2d.getX())*(punt2d.getX() - center2d.getX())) + ((punt2d.getY() -center2d.getY())*(punt2d.getY() -center2d.getY()));
            if (rek < (16*16)) {
                series1.add(toDegrees(result2.getAzimuth()), result2.getRadius());
            } else {
                System.out.println("out");
            }
        }

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
