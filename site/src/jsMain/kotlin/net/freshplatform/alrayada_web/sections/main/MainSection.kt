package net.freshplatform.alrayada_web.sections.main

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.grayscale
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.navigation.OpenLinkStrategy
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.style.breakpoint.displayBetween
import com.varabyte.kobweb.silk.style.breakpoint.displayIfAtLeast
import com.varabyte.kobweb.silk.style.breakpoint.displayUntil
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import net.freshplatform.alrayada_web.components.core.MyImage
import net.freshplatform.alrayada_web.core.data.StringRes
import net.freshplatform.alrayada_web.core.services.localization.stringResource
import net.freshplatform.alrayada_web.models.HomePageSections
import net.freshplatform.alrayada_web.models.ThemeColors
import net.freshplatform.alrayada_web.sections.main.compoments.MobileNavigation
import net.freshplatform.alrayada_web.sections.main.compoments.MyHeader
import net.freshplatform.alrayada_web.sections.main.compoments.SocialBar
import net.freshplatform.alrayada_web.styles.MainButtonStyle
import net.freshplatform.alrayada_web.utils.constants.Constants
import net.freshplatform.alrayada_web.utils.constants.PublicRes
import net.freshplatform.alrayada_web.utils.extensions.asFragmentIdentifier
import net.freshplatform.alrayada_web.utils.extensions.removeCharAtIndex
import net.freshplatform.alrayada_web.utils.isLastDayOfTheYear
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.SMOOTH
import org.w3c.dom.ScrollBehavior
import org.w3c.dom.ScrollToOptions


@Composable
fun MainSection() = Box(
    contentAlignment = Alignment.TopCenter
) {
    MainBackground()
    MainContent()
}

@Composable
private fun MainBackground() = MyImage(
    modifier = Modifier.fillMaxSize().objectFit(ObjectFit.Cover),
    src = PublicRes.Assets.Svg.BACKGROUND,
    contentDescription = "Background Image",
    lazyLoading = false
)

@Composable
private fun MainContent() = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween, // Not necessary
    horizontalAlignment = Alignment.CenterHorizontally
) {
    MyHeader() // fill max width (80 to 90 percent)
    MobileNavigation(modifier = Modifier.displayUntil(Breakpoint.LG))
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val numColumns = numColumns(base = 1, md = 2)
        val breakpoint = rememberBreakpoint()
        SimpleGrid(
            modifier = Modifier
                .displayIfAtLeast(Breakpoint.LG)
                .fillMaxHeight()
                .fillMaxWidth(
                    if (breakpoint >= Breakpoint.LG) 80.percent else 90.percent
                ),
            numColumns = numColumns
        ) {
            MainText()
            MainImage()
        }
        // For small - medium screens
//        SimpleGrid(
//            modifier = Modifier.displayBetween(Breakpoint.SM, Breakpoint.LG).fillMaxWidth(90.percent),
//            numColumns = numColumns
//        ) {
//            content()
//        }
    }
}

val helloTextStyle = CssStyle {
    base {
        Modifier
            .scale(1.0)
            .margin(topBottom = 0.px)
            .fontSize(45.px)
            .color(ThemeColors.PreviousPrimary.colorValue)
            .transition(
                Transition.of(property = "scale", duration = 200.ms),
            )
    }
    hover {
        Modifier
            .scale(1.1)
    }
}

@Composable
private fun MainText() {
    val firstChar = 'H' // This should always be change when the text changed
    var mainText by remember { mutableStateOf("Hello, We are") }

    @Composable
    fun AnimateHelloText() {
        if (isLastDayOfTheYear()) {
            return
        }
        if (window.matchMedia("(max-width: 768px").matches) {
            return
        }

        var isPlusOperator = true

        LaunchedEffect(Unit) {
            delay(2000L)
            val value = mainText
            mainText = ""

            while (true) {

                for (charIndex in value.indices) {
                    if (isPlusOperator) {
                        mainText += value[charIndex]
                        delay(200L)
                        continue
                    }
                    if (mainText[mainText.lastIndex] == firstChar) {
                        isPlusOperator = !isPlusOperator
                        continue
                    }
                    mainText = mainText.removeCharAtIndex(mainText.lastIndex)
                    delay(100L)
                }
                isPlusOperator = !isPlusOperator
            }
        }
    }
    if (Constants.ANIMATION_ENABLED) {
        AnimateHelloText()
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocialBar(modifier = Modifier.displayIfAtLeast(Breakpoint.LG))
        Column {
            P(
                attrs = helloTextStyle
                    .toModifier()
                    .displayIfAtLeast(Breakpoint.MD)
                    .toAttrs()
            ) {
                Text(mainText)
            }
            P(
                attrs = Modifier
                    .displayIfAtLeast(Breakpoint.LG)
                    .margin(top = 20.px, bottom = 0.px)
                    .fontSize(68.px)
                    .fontWeight(FontWeight.Bolder)
                    .color(ThemeColors.Secondary.colorValue)
                    .toAttrs()
            ) {
                Text(stringResource(StringRes.AlrayadaAlarabiah))
            }
            P(
                attrs = Modifier
                    .displayBetween(Breakpoint.SM, Breakpoint.LG)
                    .margin(top = 20.px, bottom = 0.px)
                    .fontSize(40.px)
                    .fontWeight(FontWeight.Bolder)
                    .color(ThemeColors.Secondary.colorValue)
                    .toAttrs()
            ) {
                Text(stringResource(StringRes.AlrayadaAlarabiah))
            }
            P(
                attrs = Modifier
                    .margin(top = 10.px, bottom = 5.px)
                    .fontSize(20.px)
                    .fontWeight(FontWeight.Bold)
                    .color(ThemeColors.Secondary.colorValue)
                    .toAttrs()
            ) {
                Text(stringResource(StringRes.CompanyProfile))
            }
            P(
                attrs = Modifier
                    .margin(bottom = 5.px)
                    .maxWidth(400.px)
                    .fontSize(15.px)
                    .fontStyle(FontStyle.Italic)
                    .fontWeight(FontWeight.Normal)
                    .color(ThemeColors.Secondary.colorValue)
                    .toAttrs()
            ) {
                Text(stringResource(StringRes.SkyOurLimits))
            }
            Button(
                attrs = MainButtonStyle.toModifier()
                    .margin(bottom = 20.px, top = 10.px)
                    .onClick {
                        // A workaround to fix bug when click inside the button but not on the link
                        val contactUsSection = document.getElementById(HomePageSections.Contact.id)
                        contactUsSection?.scrollIntoView(
                            ScrollToOptions(
                                behavior = ScrollBehavior.SMOOTH
                            )
                        )
                    }
                    .toAttrs()
            ) {
                val text = stringResource(StringRes.ContactUs)
                Link(
                    modifier = Modifier
                        .color(Colors.White)
                        .textDecorationLine(TextDecorationLine.None)
                        .fillMaxSize()
                        .title(text),
                    path = HomePageSections.Contact.id.asFragmentIdentifier(),
                    openExternalLinksStrategy = OpenLinkStrategy.IN_NEW_TAB,
                    text = text,
                )
            }
        }
    }
}

val MainImageStyle = CssStyle {
    base {
        Modifier
            .filter(grayscale(100.percent))
            .transition(Transition.of(property = "filter", duration = 240.ms))
    }
    hover {
        Modifier
            .filter(grayscale(0.percent))
    }

}

@Composable
fun MainImage() = Box(
    modifier = MainImageStyle
        .toModifier()
        .fillMaxSize(80.percent)
        .fillMaxHeight(),
    contentAlignment = Alignment.Center
) {
    MyImage(
        modifier = Modifier
            .fillMaxWidth()
            .objectFit(ObjectFit.Cover),
        src = PublicRes.Assets.Svg.MAIN,
        contentDescription = "",
        lazyLoading = false
    )
}