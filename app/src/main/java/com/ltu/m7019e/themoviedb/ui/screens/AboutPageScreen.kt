package com.ltu.m7019e.themoviedb.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import coil.compose.AsyncImage
import com.ltu.m7019e.themoviedb.R
import com.ltu.m7019e.themoviedb.ui.theme.TheMovieDBTheme
import kotlin.math.max


@Composable
fun AboutPageScreen(
    modifier: Modifier = Modifier
) {
    val matzuu = painterResource(id = R.drawable.mattias_profile_picture)
    val stigmund = painterResource(id = R.drawable.simon_profile_picture)
    val ctx = LocalContext.current
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(stringResource(R.string.github_repo))
    )
    Column (
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxHeight()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(2f),
        ) {
            ContributorCard(
                painterResource = matzuu,
                modifier = Modifier
                    .weight(1f),
                name = stringResource(R.string.mattias_name)
            )
            ContributorCard(
                painterResource = stigmund,
                modifier = Modifier.weight(1f),
                name = stringResource(R.string.simon_name)
            )
        }

        SocialLinksCard(
            ctx = ctx,
            intent = intent,
            modifier = Modifier.weight(1F)
        )
    }
}

@Composable
fun SocialLinksCard(
    ctx: Context,
    intent: Intent,
    modifier: Modifier = Modifier
) {
    Row (
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column (
            modifier = Modifier
                .clickable { ctx.startActivity(intent) }
        ) {
            Box (modifier = Modifier.weight(1f)) {
                AsyncImage(
                    model = R.drawable.github_icon,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )
            }
            Text(
                text = "Github",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterHorizontally),
            )
        }
    }
}

@Composable
fun ContributorCard(
    painterResource: Painter,
    name: String,
    contentDesc: String? = null,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(8.dp)
    ) {
        val polygon = remember {
            RoundedPolygon(
                6,
                rounding = CornerRounding(1f),
            )
        }
        val clip = remember(polygon) {
            RoundedPolygonShape(polygon = polygon)
        }
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Image(
                painter = painterResource,
                contentDescription = contentDesc,
                modifier = Modifier
                    .clip(clip)
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.weight(1f)
        )
    }
}



// copied from https://developer.android.com/develop/ui/compose/graphics/draw/shapes
class RoundedPolygonShape(
    private val polygon: RoundedPolygon,
    private var matrix: Matrix = Matrix()
) : Shape {
    private fun RoundedPolygon.getBounds() = calculateBounds().let { Rect(it[0], it[1], it[2], it[3]) }

    private var path = Path()
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        path.rewind()
        path = polygon.toPath().asComposePath()
        matrix.reset()
        val bounds = polygon.getBounds()
        val maxDimension = max(bounds.width, bounds.height)
        matrix.scale(size.width / maxDimension, size.height / maxDimension)
        matrix.translate(-bounds.left, -bounds.top)

        path.transform(matrix)
        return Outline.Generic(path)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AboutPageScreenPreview(){
    TheMovieDBTheme {
        AboutPageScreen()
    }
}
