package com.example.zenword;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.Iterator;

import DataStructures.*;


public class ParaulesOcultes
{
    private final MainActivity mainActivity;
    private final int[] guidelines;
    private TextView[][] textViews;


    /**
     * Constructor de la classe ParaulesOcultes
     * @param mainActivity activitat principal de l'aplicació
     */
    public ParaulesOcultes(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
        guidelines = new int[6];

        //Comprovam si la disposició de la pantalla és vertical o horitzontal
        if (MainActivity.isVertical)
        {
            guidelines[0] = mainActivity.findViewById(R.id.guidelineHor1).getId();
            guidelines[1] = mainActivity.findViewById(R.id.guidelineHor2).getId();
            guidelines[2] = mainActivity.findViewById(R.id.guidelineHor3).getId();
            guidelines[3] = mainActivity.findViewById(R.id.guidelineHor4).getId();
            guidelines[4] = mainActivity.findViewById(R.id.guidelineHor5).getId();
            guidelines[5] = mainActivity.findViewById(R.id.guidelineHor6).getId();
        }
        else
        {
            guidelines[0] = mainActivity.findViewById(R.id.guidelineHor1).getId();
            guidelines[1] = mainActivity.findViewById(R.id.guidelineHor2).getId();
            guidelines[2] = mainActivity.findViewById(R.id.guidelineHor3).getId();
            guidelines[3] = mainActivity.findViewById(R.id.guidelineHor4).getId();
            guidelines[4] = mainActivity.findViewById(R.id.guidelineHor5).getId();
            guidelines[5] = mainActivity.findViewById(R.id.guidelineHor6).getId();
        }
    }


    public void crear(Paraules paraules)
    {
        textViews = new TextView[paraules.getNumFiles()][Paraules.maxWordLength];
        UnsortedArrayMapping<String, Integer> ajudesDisponibles = paraules.getAjudesDisponibles();
        UnsortedArrayMapping<String, Integer> ajudesActuals = paraules.getAjudesActuals();

        Iterator it = paraules.getParaulesOcultes().iterator();
        while (it.hasNext())
        {
            UnsortedArrayMapping.Pair pair = (UnsortedArrayMapping.Pair) it.next();
            int i = (int) pair.getValue();
            String s = (String) pair.getKey();

            textViews[i] = crearFilaTextViews(i, s.length());
            if (ajudesActuals.get(s) != null) mostraPrimeraLletra(s, i);
            else ajudesDisponibles.put(s, i);
        }

        it = paraules.getParaulesTrobades().iterator();
        while (it.hasNext())
        {
            UnsortedArrayMapping.Pair pair = (UnsortedArrayMapping.Pair) it.next();
            int i = (int) pair.getValue();
            String s = paraules.getParaulaFormatada((String) pair.getKey());

            textViews[i] = crearFilaTextViews(i, s.length());
            for (int j = 0; j< textViews[i].length; j++)
            {
                textViews[i][j].setText(String.valueOf(s.charAt(j)).toUpperCase());
            }
        }
    }


    /**
     * Crea una fila de TextViews que servirà per mostrar una paraula
     * @param guia número de guideline on crear la fila
     * @param lletres número de lletres que s'han de mostrar
     * @return array de TextViews correctament col.locat
     */
    private TextView[] crearFilaTextViews(int guia, int lletres)
    {
        TextView[] param = new TextView[lletres];
        ConstraintLayout constraint = mainActivity.findViewById(R.id.main);

        //Definim les propietats de cada TextView
        for (int i=0; i<lletres; i++)
        {
            int id = View.generateViewId();

            param[i] = new TextView (mainActivity);
            param[i].setId(id);
            param[i].setText("");
            param[i].setGravity(Gravity.CENTER);
            param[i].setTextSize(MainActivity.textSize);
            param[i].setTextColor(Color.parseColor("#FFFFFF"));
            param[i].setBackgroundResource(R.drawable.letter_box);
            param[i].setBackgroundTintList(ColorStateList.valueOf(MainActivity.color));
            constraint.addView(param[i]);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraint);

            //Actualitzam les dimensions en funció de la disposició de la pantalla
            int wdth, startId, maxHeight;
            if (MainActivity.isVertical)
            {
                wdth = (int) ((MainActivity.dpWidth-28)*MainActivity.density/7);
                startId = ConstraintSet.PARENT_ID;
                maxHeight = (int) (MainActivity.dpHeight*0.07*MainActivity.density) -8;
                //constraintSet.constrainMaxHeight(id, (int) MainActivity.dpHeight/6);
            }
            else
            {
                wdth = (int) (((MainActivity.dpWidth-28)*MainActivity.density*0.4)/7);
                startId = mainActivity.findViewById(R.id.guidelineVer1).getId();
                maxHeight = (int) (MainActivity.dpHeight*0.2*MainActivity.density) -8;
                constraintSet.constrainMaxHeight(id, (int) (MainActivity.dpWidth/5));
            }

            int margin = (int) ((wdth*(7-lletres)+28*MainActivity.density)/2);

            constraintSet.connect(id, ConstraintSet.BOTTOM, guidelines[guia+1], ConstraintSet.TOP,4);
            constraintSet.connect(id, ConstraintSet.TOP, guidelines[guia], ConstraintSet.BOTTOM,4);

            constraintSet.constrainHeight(id, maxHeight);
            constraintSet.constrainWidth(id, wdth);

            //Definim el constraint dels TextViews
            if (i == 0)
            {
                constraintSet.connect(id, ConstraintSet.START, startId, ConstraintSet.START, margin);
            }
            else
            {
                constraintSet.connect(id, ConstraintSet.START, param[i-1].getId(), ConstraintSet.END, 2);
            }

            constraintSet.applyTo(constraint);
        }

        return param;
    }


    /**
     * Mostra una paraula sencera
     * @param s paraula a mostrar
     * @param posicio posició de la paraula
     */
    public void mostraParaula(String s, int posicio)
    {
        for (int i = 0; i< textViews[posicio].length; i++)
        {
            String c = String.valueOf(s.charAt(i)).toUpperCase();
            textViews[posicio][i].setText(c);
        }
    }


    /**
     * Mostra la primera lletra d'una paraula
     * @param s paraula a mostrar
     * @param posicio posició de la paraula
     */
    public void mostraPrimeraLletra(String s, int posicio)
    {
        String c = String.valueOf(s.charAt(0)).toLowerCase();
        textViews[posicio][0].setText(c);
    }


    /**
     * Desactiva la visibilitat de les lletres
     */
    public void amaga()
    {
        for (TextView[] wordsTextView : textViews)
        {
            for (TextView aux : wordsTextView) aux.setVisibility(View.GONE);
        }
    }

}
