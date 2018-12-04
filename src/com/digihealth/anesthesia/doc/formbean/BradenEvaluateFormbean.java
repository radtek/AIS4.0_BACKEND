package com.digihealth.anesthesia.doc.formbean;

import java.util.List;

import com.digihealth.anesthesia.doc.po.DocBradenDetail;
import com.digihealth.anesthesia.doc.po.DocBradenEvaluate;

public class BradenEvaluateFormbean
{
    private DocBradenEvaluate bradenEvaluate;
    
    private List<DocBradenDetail> bradenDetailList;

    public DocBradenEvaluate getBradenEvaluate()
    {
        return bradenEvaluate;
    }

    public void setBradenEvaluate(DocBradenEvaluate bradenEvaluate)
    {
        this.bradenEvaluate = bradenEvaluate;
    }

    public List<DocBradenDetail> getBradenDetailList()
    {
        return bradenDetailList;
    }

    public void setBradenDetailList(List<DocBradenDetail> bradenDetailList)
    {
        this.bradenDetailList = bradenDetailList;
    }
}
