package sonar.logistics.client.gsi.components.layouts.flexbox;

import sonar.logistics.client.gsi.api.IComponent;
import sonar.logistics.client.gsi.components.layouts.Layout;
import sonar.logistics.client.gsi.components.layouts.yoga.YogaRectTest;
import sonar.logistics.client.gsi.style.StyleHelper;
import sonar.logistics.client.vectors.Quad2D;

import java.util.ArrayList;
import java.util.List;

public class PassiveFlexBox extends Layout {


    public void buildComponents(Quad2D bounds, List<IComponent> components){
        List<YogaRectTest> rects = new ArrayList<>();
        components.stream().forEach(c -> {
            if(c instanceof YogaRectTest){
                rects.add((YogaRectTest) c);
            }
        });

        buildYogaComponents(bounds, rects);
    }

    public void buildYogaComponents(Quad2D bounds, List<YogaRectTest> rects){



        //9.1 Initial Setup - SKIPPED - Already setup.


        //9.2 Line Length Determination

        ////2. Determine the available main and cross space for the flex items
        float availableMainSpace = (float)bounds.getWidth();
        float availableCrossSpace = (float)bounds.getHeight();


        ////3. Determine the flex base size and hypothetical main size of each item:
        for(YogaRectTest rect : rects){

            float flexBaseSize = 0;
            float hypotheticalMainSize = 0;

            //TODO FLEX BASE SIZE, FOR NOW 0!

            float rectMinSize = StyleHelper.getLengthSafeF(rect.flex.getMinWidth(), bounds.getWidth());
            float rectMaxSize = StyleHelper.getLengthSafeF(rect.flex.getMinWidth(), bounds.getWidth());
            hypotheticalMainSize = Math.max(rectMinSize, Math.min(flexBaseSize, rectMaxSize));
        }

        ////4. Determine the main size of the flex container
        float containerMainSize = availableMainSpace; //TODO?





        //9.3. Main Size Determination

        ////5. Collect flex items into flex lines
        ///TODO similar to my wrapping code!!!

        ////6. Resolve the flexible lenths
        ///for all items find their used main size.



        //9.4 Cross Size Determination

        ////7. Determine the hypothetical cross size of each item

        ////8. Calculate the cross size of each flex line.

        ////9. Handle 'align-content: stretch'

        ////10. Collapse visibility:collapse items.

        ////11. Determine the used cross size of each flex item



        //9.5. Main-Axis Alignment

        ////12. Distribute any remaining free space. For each flex line:




        //9.6 Cross-Axis Alignment

        ////13. Resolve cross-axis auto margins.




        //9.7 Resolving Flexible Lengths


        ////1. Determin the used flex factor

        ////2. Size inflexible itmes.

        ////3. Calculator initial free space

        ////4. Loop

        ////5. Set each item’s used main size to its target main size.


        //9.8 Definite and Indefinite Sizes




    }


}
