/*
   Copyright (c) 2017 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.lienzo.ks.client.views.components;

import static com.ait.lienzo.client.core.shape.wires.LayoutContainer.Layout.CENTER;

import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.IContainer;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.MultiPathDecorator;
import com.ait.lienzo.client.core.shape.OrthogonalPolyLine;
import com.ait.lienzo.client.core.shape.Star;
import com.ait.lienzo.client.core.shape.wires.IConnectionAcceptor;
import com.ait.lienzo.client.core.shape.wires.IContainmentAcceptor;
import com.ait.lienzo.client.core.shape.wires.MagnetManager;
import com.ait.lienzo.client.core.shape.wires.WiresConnection;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresMagnet;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.ks.client.views.AbstractViewComponent;

public class WiresArrowsViewComponent extends AbstractViewComponent
{
    public WiresArrowsViewComponent()
    {
        final Layer layer = new Layer();

        final WiresManager wiresManager = WiresManager.get(layer);

        final double w = 100;

        final double h = 100;

        wiresManager.setConnectionAcceptor(new IConnectionAcceptor()
        {
            @Override
            public boolean headConnectionAllowed(final WiresConnection head, final WiresShape shape)
            {
                final WiresConnection tail = head.getConnector().getTailConnection();

                final WiresMagnet m = tail.getMagnet();

                if (m == null)
                {
                    return true;
                }
                if (shape == null)
                {
                    return true;
                }
                return accept(shape.getContainer(), tail.getMagnet().getMagnets().getGroup());
            }

            @Override
            public boolean tailConnectionAllowed(final WiresConnection tail, final WiresShape shape)
            {
                final WiresConnection head = tail.getConnector().getHeadConnection();

                final WiresMagnet m = head.getMagnet();

                if (m == null)
                {
                    return true;
                }
                if (shape == null)
                {
                    return true;
                }
                return accept(head.getMagnet().getMagnets().getGroup(), shape.getContainer());
            }

            @Override
            public boolean acceptHead(final WiresConnection head, final WiresMagnet magnet)
            {
                final WiresConnection tail = head.getConnector().getTailConnection();

                final WiresMagnet m = tail.getMagnet();

                if (m == null)
                {
                    return true;
                }
                if (magnet == null)
                {
                    return true;
                }
                return accept(magnet.getMagnets().getGroup(), tail.getMagnet().getMagnets().getGroup());
            }

            @Override
            public boolean acceptTail(final WiresConnection tail, final WiresMagnet magnet)
            {
                final WiresConnection head = tail.getConnector().getHeadConnection();

                final WiresMagnet m = head.getMagnet();

                if (m == null)
                {
                    return true;
                }
                if (magnet == null)
                {
                    return true;
                }
                return accept(head.getMagnet().getMagnets().getGroup(), magnet.getMagnets().getGroup());
            }

            private boolean accept(final IContainer<?, ?> head, final IContainer<?, ?> tail)
            {
                return head.getUserData().equals(tail.getUserData());
            }
        });

        // A shape can only contain shapes of different letters for UserData

        wiresManager.setContainmentAcceptor(new IContainmentAcceptor()
        {
            @Override
            public boolean containmentAllowed(final WiresContainer parent, final WiresShape[] children)
            {
                return acceptContainment(parent, children);
            }

            @Override
            public boolean acceptContainment(final WiresContainer parent, final WiresShape[] children)
            {
                if (parent.getParent() == null)
                {
                    return true;
                }
                return !parent.getContainer().getUserData().equals(children[0].getContainer().getUserData());
            }
        });

        final WiresShape wiresShape0 = new WiresShape(new MultiPath().rect(0, 0, w, h).setStrokeColor("#CC0000")).setLocation(new Point2D(400, 400)).setDraggable(true);
        wiresShape0.getContainer().setUserData("A");
        wiresShape0.addChild(new Circle(30), CENTER);
        wiresManager.register(wiresShape0);
        wiresManager.getMagnetManager().createMagnets(wiresShape0);

        final WiresShape wiresShape1 = new WiresShape(new MultiPath().rect(0, 0, w, h).setStrokeColor("#00CC00")).setLocation(new Point2D(50, 50)).setDraggable(true);
        wiresShape1.getContainer().setUserData("A");
        wiresShape1.addChild(new Star(5, 15, 40), CENTER);
        wiresManager.register(wiresShape1);
        wiresManager.getMagnetManager().createMagnets(wiresShape1);

        final WiresShape wiresShape2 = new WiresShape(new MultiPath().rect(0, 0, 300, 200).setStrokeColor("#0000CC")).setLocation(new Point2D(50, 100)).setDraggable(true);
        wiresShape2.getContainer().setUserData("B");
        wiresManager.register(wiresShape2);
        wiresManager.getMagnetManager().createMagnets(wiresShape2);

        // bolt
        final String svg = "M 0 100 L 65 115 L 65 105 L 120 125 L 120 115 L 200 180 L 140 160 L 140 170 L 85 150 L 85 160 L 0 140 Z";
        final WiresShape wiresShape3 = new WiresShape(new MultiPath(svg).setStrokeColor("#0000CC")).setLocation(new Point2D(50, 300)).setDraggable(true);
        wiresShape3.getContainer().setUserData("B");
        wiresManager.register(wiresShape3);
        wiresManager.getMagnetManager().createMagnets(wiresShape3);

        connect(wiresShape1.getMagnets(), wiresShape0.getMagnets(), wiresManager);

        getLienzoPanel().add(layer);

        getLienzoPanel().setBackgroundLayer(getBackgroundLayer());

        getWorkingContainer().add(getLienzoPanel());
    }

    private void connect(final MagnetManager.Magnets magnets0, final MagnetManager.Magnets magnets1, final WiresManager wiresManager)
    {
        final WiresMagnet m0_1 = magnets0.getMagnet(3);
        final WiresMagnet m1_1 = magnets1.getMagnet(7);

        double x0, x1, y0, y1;

        final MultiPath head = new MultiPath();
        head.M(15, 20);
        head.L(0, 20);
        head.L(15 / 2, 0);
        head.Z();

        final MultiPath tail = new MultiPath();
        tail.M(15, 20);
        tail.L(0, 20);
        tail.L(15 / 2, 0);
        tail.Z();

        OrthogonalPolyLine line;
        x0 = m0_1.getControl().getX();
        y0 = m0_1.getControl().getY();
        x1 = m1_1.getControl().getX();
        y1 = m1_1.getControl().getY();
        line = createLine(x0, y0, (x0 + ((x1 - x0) / 2)), (y0 + ((y1 - y0) / 2)), x1, y1);
        line.setHeadOffset(head.getBoundingBox().getHeight());
        line.setTailOffset(tail.getBoundingBox().getHeight());
        line.setSelectionBoundsOffset(15);

        final WiresConnector connector0 = new WiresConnector(m0_1, m1_1, line, new MultiPathDecorator(head), new MultiPathDecorator(tail));
        wiresManager.register(connector0);

        head.setStrokeWidth(5).setStrokeColor("#0000CC");
        tail.setStrokeWidth(5).setStrokeColor("#0000CC");
        line.setStrokeWidth(5).setStrokeColor("#0000CC");
    }

    private OrthogonalPolyLine createLine(final double... points)
    {
        return new OrthogonalPolyLine(Point2DArray.fromArrayOfDouble(points)).setCornerRadius(5).setDraggable(true);
    }
}
