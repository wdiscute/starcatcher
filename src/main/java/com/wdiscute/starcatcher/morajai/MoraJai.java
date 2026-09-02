package com.wdiscute.starcatcher.morajai;

import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.HashMap;
import java.util.Map;

public class MoraJai
{
    public enum Color
    {
        GRAY(0xffa7a7a7),
        BLACK(0xff282828),
        GREEN(0xff1b8529),
        ORANGE(0xffb1712b),
        PURPLE(0xff762485),
        YELLOW(0xffbcba2f),
        PINK(0xffbe79c4);

        final int color;

        Color(int color)
        {
            this.color = color;
        }
    }

    public enum Pos
    {
        TOP_LEFT(-85, -85),
        TOP_MIDDLE(-25, -85),
        TOP_RIGHT(35, -85),

        MIDDLE_LEFT(-85, -25),
        MIDDLE_MIDDLE(-25, -25),
        MIDDLE_RIGHT(35, -25),

        BOTTOM_LEFT(-85, 35),
        BOTTOM_MIDDLE(-25, 35),
        BOTTOM_RIGHT(35, 35);

        final int offsetX;
        final int offsetY;

        Pos(int offsetX, int offsetY)
        {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        public boolean isTop()
        {
            return this == TOP_LEFT || this == TOP_MIDDLE || this == TOP_RIGHT;
        }

        public boolean isMiddle()
        {
            return this == MIDDLE_LEFT || this == MIDDLE_MIDDLE || this == MIDDLE_RIGHT;
        }

        public boolean isBottom()
        {
            return this == BOTTOM_LEFT || this == BOTTOM_MIDDLE || this == BOTTOM_RIGHT;
        }
    }

    public static class Grid
    {
        public final int SIZE = 50;

        Map<Pos, Behaviour> map = new HashMap<>()
        {{
            put(Pos.TOP_LEFT, BLACK);
            put(Pos.TOP_MIDDLE, GREEN);
            put(Pos.TOP_RIGHT, YELLOW);

            put(Pos.MIDDLE_LEFT, BLACK);
            put(Pos.MIDDLE_MIDDLE, BLACK);
            put(Pos.MIDDLE_RIGHT, BLACK);

            put(Pos.BOTTOM_LEFT, GRAY);
            put(Pos.BOTTOM_MIDDLE, GRAY);
            put(Pos.BOTTOM_RIGHT, GRAY);
        }};

        public void swap(Pos pos1, Pos pos2)
        {
            Behaviour pos1Old = map.get(pos1);
            map.put(pos1, map.get(pos2));
            map.put(pos2, pos1Old);
        }

        public void render(GuiGraphicsExtractor guiGraphics)
        {
            for (Map.Entry<Pos, Behaviour> entry : map.entrySet())
            {
                Pos k = entry.getKey();
                ScreenUtils.fill(guiGraphics, k.offsetX, k.offsetY, SIZE, SIZE, entry.getValue().getColor().color);
            }
        }

        public boolean click(double x, double y)
        {
            for (Map.Entry<Pos, Behaviour> entry : map.entrySet())
            {
                Pos k = entry.getKey();
                Behaviour v = entry.getValue();
                if (x > k.offsetX && x < k.offsetX + SIZE && y > k.offsetY && y < k.offsetY + SIZE)
                {
                    v.click(this, k);
                    return true;
                }
            }
            return false;
        }
    }

    public static final Behaviour GRAY = new Behaviour()
    {
        @Override
        public Color getColor()
        {
            return Color.GRAY;
        }

        @Override
        public void click(Grid grid, Pos pos)
        {
            //no behaviour
        }
    };

    public static final Behaviour BLACK = new Behaviour()
    {
        @Override
        public Color getColor()
        {
            return Color.BLACK;
        }

        @Override
        public void click(Grid grid, Pos pos)
        {
            if (pos.isTop())
            {
                grid.swap(Pos.TOP_RIGHT, Pos.TOP_MIDDLE);
                grid.swap(Pos.TOP_MIDDLE, Pos.TOP_LEFT);
            }

            if (pos.isMiddle())
            {
                grid.swap(Pos.MIDDLE_RIGHT, Pos.MIDDLE_MIDDLE);
                grid.swap(Pos.MIDDLE_MIDDLE, Pos.MIDDLE_LEFT);
            }

            if (pos.isBottom())
            {
                grid.swap(Pos.BOTTOM_RIGHT, Pos.BOTTOM_MIDDLE);
                grid.swap(Pos.BOTTOM_MIDDLE, Pos.BOTTOM_LEFT);
                ;
            }
        }
    };

    public static final Behaviour YELLOW = new Behaviour()
    {
        @Override
        public Color getColor()
        {
            return Color.YELLOW;
        }

        @Override
        public void click(Grid grid, Pos pos)
        {
            if (pos == Pos.BOTTOM_LEFT)
                grid.swap(pos, Pos.MIDDLE_LEFT);

            if (pos == Pos.BOTTOM_MIDDLE)
                grid.swap(pos, Pos.MIDDLE_MIDDLE);

            if (pos == Pos.BOTTOM_RIGHT)
                grid.swap(pos, Pos.MIDDLE_RIGHT);

            if (pos == Pos.MIDDLE_LEFT)
                grid.swap(pos, Pos.TOP_LEFT);

            if (pos == Pos.MIDDLE_MIDDLE)
                grid.swap(pos, Pos.TOP_MIDDLE);

            if (pos == Pos.MIDDLE_RIGHT)
                grid.swap(pos, Pos.TOP_RIGHT);

        }
    };

    public static final Behaviour PURPLE = new Behaviour()
    {
        @Override
        public Color getColor()
        {
            return Color.PURPLE;
        }

        @Override
        public void click(Grid grid, Pos pos)
        {
            if (pos == Pos.TOP_LEFT)
                grid.swap(pos, Pos.MIDDLE_LEFT);

            if (pos == Pos.TOP_MIDDLE)
                grid.swap(pos, Pos.MIDDLE_MIDDLE);

            if (pos == Pos.TOP_RIGHT)
                grid.swap(pos, Pos.MIDDLE_RIGHT);

            if (pos == Pos.MIDDLE_LEFT)
                grid.swap(pos, Pos.BOTTOM_LEFT);

            if (pos == Pos.MIDDLE_MIDDLE)
                grid.swap(pos, Pos.BOTTOM_MIDDLE);

            if (pos == Pos.MIDDLE_RIGHT)
                grid.swap(pos, Pos.BOTTOM_RIGHT);

        }
    };

    public static final Behaviour GREEN = new Behaviour()
    {
        @Override
        public Color getColor()
        {
            return Color.GREEN;
        }

        @Override
        public void click(Grid grid, Pos pos)
        {
            if (pos == Pos.TOP_LEFT)
                grid.swap(pos, Pos.BOTTOM_RIGHT);

            if (pos == Pos.TOP_MIDDLE)
                grid.swap(pos, Pos.BOTTOM_MIDDLE);

            if (pos == Pos.TOP_RIGHT)
                grid.swap(pos, Pos.BOTTOM_LEFT);

            if (pos == Pos.MIDDLE_LEFT)
                grid.swap(pos, Pos.MIDDLE_RIGHT);

            if (pos == Pos.MIDDLE_RIGHT)
                grid.swap(pos, Pos.MIDDLE_LEFT);

            if (pos == Pos.BOTTOM_LEFT)
                grid.swap(pos, Pos.TOP_RIGHT);

            if (pos == Pos.BOTTOM_RIGHT)
                grid.swap(pos, Pos.TOP_LEFT);

            if (pos == Pos.BOTTOM_MIDDLE)
                grid.swap(pos, Pos.TOP_MIDDLE);
        }
    };

    public static final Behaviour ORANGE = new Behaviour()
    {
        @Override
        public Color getColor()
        {
            return Color.ORANGE;
        }

        @Override
        public void click(Grid grid, Pos pos)
        {
            //corners
            if (pos == Pos.TOP_LEFT)
                if(grid.map.get(Pos.TOP_MIDDLE) == grid.map.get(Pos.MIDDLE_LEFT))
                    grid.map.put(pos, grid.map.get(Pos.TOP_MIDDLE));

            if (pos == Pos.TOP_RIGHT)
                if(grid.map.get(Pos.TOP_MIDDLE) == grid.map.get(Pos.MIDDLE_RIGHT))
                    grid.map.put(pos, grid.map.get(Pos.TOP_MIDDLE));

            if (pos == Pos.BOTTOM_LEFT)
                if(grid.map.get(Pos.BOTTOM_MIDDLE) == grid.map.get(Pos.MIDDLE_LEFT))
                    grid.map.put(pos, grid.map.get(Pos.BOTTOM_MIDDLE));

            if (pos == Pos.BOTTOM_RIGHT)
                if(grid.map.get(Pos.BOTTOM_MIDDLE) == grid.map.get(Pos.MIDDLE_RIGHT))
                    grid.map.put(pos, grid.map.get(Pos.BOTTOM_MIDDLE));

            //not corners
            if (pos == Pos.TOP_MIDDLE)
            {
                if(grid.map.get(Pos.TOP_LEFT) == grid.map.get(Pos.TOP_RIGHT))
                    grid.map.put(pos, grid.map.get(Pos.TOP_LEFT));

                if(grid.map.get(Pos.TOP_LEFT) == grid.map.get(Pos.MIDDLE_MIDDLE))
                    grid.map.put(pos, grid.map.get(Pos.MIDDLE_MIDDLE));

                if(grid.map.get(Pos.TOP_RIGHT) == grid.map.get(Pos.MIDDLE_MIDDLE))
                    grid.map.put(pos, grid.map.get(Pos.MIDDLE_MIDDLE));
            }

            if (pos == Pos.BOTTOM_MIDDLE)
            {
                if(grid.map.get(Pos.BOTTOM_LEFT) == grid.map.get(Pos.BOTTOM_RIGHT))
                    grid.map.put(pos, grid.map.get(Pos.BOTTOM_LEFT));

                if(grid.map.get(Pos.BOTTOM_LEFT) == grid.map.get(Pos.MIDDLE_MIDDLE))
                    grid.map.put(pos, grid.map.get(Pos.MIDDLE_MIDDLE));

                if(grid.map.get(Pos.BOTTOM_RIGHT) == grid.map.get(Pos.MIDDLE_MIDDLE))
                    grid.map.put(pos, grid.map.get(Pos.MIDDLE_MIDDLE));
            }

            if (pos == Pos.MIDDLE_LEFT)
            {
                if(grid.map.get(Pos.TOP_LEFT) == grid.map.get(Pos.BOTTOM_LEFT))
                    grid.map.put(pos, grid.map.get(Pos.BOTTOM_LEFT));

                if(grid.map.get(Pos.TOP_LEFT) == grid.map.get(Pos.MIDDLE_MIDDLE))
                    grid.map.put(pos, grid.map.get(Pos.MIDDLE_MIDDLE));

                if(grid.map.get(Pos.BOTTOM_LEFT) == grid.map.get(Pos.MIDDLE_MIDDLE))
                    grid.map.put(pos, grid.map.get(Pos.MIDDLE_MIDDLE));
            }

            if (pos == Pos.MIDDLE_RIGHT)
            {
                if(grid.map.get(Pos.TOP_RIGHT) == grid.map.get(Pos.BOTTOM_RIGHT))
                    grid.map.put(pos, grid.map.get(Pos.BOTTOM_RIGHT));

                if(grid.map.get(Pos.TOP_RIGHT) == grid.map.get(Pos.MIDDLE_MIDDLE))
                    grid.map.put(pos, grid.map.get(Pos.MIDDLE_MIDDLE));

                if(grid.map.get(Pos.BOTTOM_RIGHT) == grid.map.get(Pos.MIDDLE_MIDDLE))
                    grid.map.put(pos, grid.map.get(Pos.MIDDLE_MIDDLE));
            }
        }
    };

    public interface Behaviour
    {
        Color getColor();

        void click(Grid grid, Pos pos);
    }
}
