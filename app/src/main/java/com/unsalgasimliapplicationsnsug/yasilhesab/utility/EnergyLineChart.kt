package com.unsalgasimliapplicationsnsug.yasilhesab.screens

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.unsalgasimliapplicationsnsug.yasilhesab.R
import com.unsalgasimliapplicationsnsug.yasilhesab.model.ChartPoint
import com.unsalgasimliapplicationsnsug.yasilhesab.model.DashboardFullDataViewModel

@Composable
fun EnergyLineChartSection(
    chartData: List<ChartPoint>,
    filters: Array<DashboardFullDataViewModel.TimeFilter>,
    selectedFilterIdx: Int,
    onFilterSelected: (DashboardFullDataViewModel.TimeFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val background = MaterialTheme.colorScheme.background

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(background)
            .padding(vertical = 0.dp)
    ) {
        // Qrafik - ekranın tam eni, mümkün qədər az boşluq
        Box(
            Modifier
                .fillMaxWidth()
                .height(164.dp)
                .background(background)
        ) {
            EnergyLineChartWithFade (
                modifier = Modifier
                    .fillMaxSize(),
                chartData = chartData
            )
        }
      //  Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            filters.forEachIndexed { idx, filter ->
                val selected = idx == selectedFilterIdx
                Text(
                    text = filter.label,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.13f)
                            else ComposeColor.Transparent
                        )
                        .clickable { onFilterSelected(filter) }
                        .padding(horizontal = 15.dp, vertical =24.dp)
                )
                if (idx != filters.lastIndex) Spacer(Modifier.width(6.dp))
            }
        }
        Spacer(Modifier.height(2.dp))
    }
}

@Composable
fun EnergyLineChartWithFade(
    modifier: Modifier = Modifier,
    chartData: List<ChartPoint>
) {
    val fadeWidth = 36.dp // Fade sahəsinin eni, dizaynına görə dəyişə bilərsən

    Box(modifier) {
        // Əsas qrafik
        EnergyLineChart(
            modifier = Modifier.matchParentSize(),
            chartData = chartData
        )
        // Fade overlays
        Row(
            Modifier
                .matchParentSize(), // Box-un ölçüsünü tam tutur
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Sol fade (başlanğıc)
            Box(
                Modifier
                    .width(fadeWidth)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background, // Yaxın şəffaf deyil
                                MaterialTheme.colorScheme.background.copy(alpha = 0f) // Tam şəffaf
                            )
                        )
                    )
            )
            // Sağ fade (son)
            Box(
                Modifier
                    .width(fadeWidth)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background.copy(alpha = 0f), // Tam şəffaf
                                MaterialTheme.colorScheme.background // Yaxın şəffaf deyil
                            )
                        )
                    )
            )
        }
    }
}


@Composable
fun EnergyLineChart(
    modifier: Modifier = Modifier,
    chartData: List<ChartPoint>
) {
    val context = LocalContext.current
    val background = MaterialTheme.colorScheme.background

    if (chartData.size < 2) {
        AndroidView(
            modifier = modifier,
            factory = { ctx -> LineChart(ctx).apply { setNoDataText("No data") } }
        )
        return
    }

    val entries = chartData.mapIndexed { i, point -> Entry(i.toFloat(), point.value, point.millis) }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            LineChart(ctx).apply {
                setBackgroundColor(background.toArgb())
                setViewPortOffsets(0f, 0f, 0f, 0f)
                setDrawGridBackground(false)
                setDrawBorders(false)
                isDoubleTapToZoomEnabled = false
                setPinchZoom(false)
                setScaleEnabled(false)
                description.isEnabled = false
                legend.isEnabled = false
                xAxis.isEnabled = false
                axisLeft.isEnabled = false
                axisRight.isEnabled = false
                marker = EnergyMarkerView(ctx)
                isHighlightPerTapEnabled = true
                isHighlightPerDragEnabled = true
                extraBottomOffset = 0f
                extraTopOffset = 0f
            }
        },
        update = { chart ->
            val dataSet = LineDataSet(entries, null).apply {
                color = Color.parseColor("#9149FF")
                lineWidth = 3.2f
                setDrawCircles(false)
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(Color.parseColor("#4695FFFE"), Color.TRANSPARENT)
                )
                highLightColor = Color.parseColor("#7E1DFE")
                setDrawHighlightIndicators(true)
                highlightLineWidth = 2.6f
                setDrawHorizontalHighlightIndicator(false)
            }
            chart.data = LineData(dataSet)
            chart.marker = EnergyMarkerView(chart.context)
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    )
}


class EnergyMarkerView(
    context: Context
) : MarkerView(context, R.layout.marker_view_layout) {

    private val textView: TextView = findViewById(R.id.marker_text)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e == null) {
            textView.visibility = View.GONE
            return
        }
        val millis = (e.data as? Long) ?: 0L
        val value = e.y
        val label = android.text.format.DateFormat.format("dd.MM HH:mm", millis)
        textView.text = "$label\n${"%.2f".format(value)} kWh"
        textView.visibility = View.VISIBLE
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): com.github.mikephil.charting.utils.MPPointF {
        // Marker tam ortalanır, label nöqtənin üstündə çıxır
        return com.github.mikephil.charting.utils.MPPointF(-(width / 2f), -height.toFloat())
    }
}

// Compose rəngini Android rənginə çevirən extension
fun ComposeColor.toArgb(): Int = android.graphics.Color.argb(
    (this.alpha * 255).toInt(),
    (this.red * 255).toInt(),
    (this.green * 255).toInt(),
    (this.blue * 255).toInt()
)
